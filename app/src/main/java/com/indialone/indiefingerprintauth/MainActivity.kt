package com.indialone.indiefingerprintauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var authButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authButton = findViewById(R.id.btn_auth)
        authButton.setOnClickListener {
            val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Please Verify User")
                .setDescription("User Authentication Required to Proceed")
                .setNegativeButtonText("Cancel")
                .build()

            getPrompt().authenticate(promptInfo)

        }


    }

    private fun getPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)
        val authCallback: BiometricPrompt.AuthenticationCallback =
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@MainActivity, SecretActivity::class.java))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    notifyUser("Authentication Required")
                }
            }

        return BiometricPrompt(this@MainActivity, executor, authCallback)
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}