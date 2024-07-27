package com.progammer.unimakinventory

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            when {
                email.isEmpty() -> {
                    showToast("Email cannot be empty")
                }
                password.isEmpty() -> {
                    showToast("Password cannot be empty")
                }
                confirmPassword.isEmpty() -> {
                    showToast("Confirm Password cannot be empty")
                }
                password != confirmPassword -> {
                    showToast("Passwords do not match")
                }
                else -> {
                    registerUser(email, password)
                }
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        // Show a progress dialog or similar to indicate that registration is in progress
        // val progressDialog = ProgressDialog(this)
        // progressDialog.setMessage("Registering...")
        // progressDialog.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Dismiss the progress dialog
                // progressDialog.dismiss()

                if (task.isSuccessful) {
                    // Registration successful, navigate to login activity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    // Registration failed, show error message
                    showToast("Registration failed: ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
