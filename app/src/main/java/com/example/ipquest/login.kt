package com.example.ipquest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity" // Add a tag for logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)

        // Log when the activity is created
        Log.d(TAG, "LoginActivity created")

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Log the values entered by the user
            Log.d(TAG, "Attempting login with email: $email and password: $password")

            // Retrieve saved credentials from SharedPreferences
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val savedEmail = sharedPref.getString("email", "")
            val savedPassword = sharedPref.getString("password", "")

            Log.d(TAG, "Retrieved saved email: $savedEmail, saved password: $savedPassword")

            // Perform login validation
            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (email == savedEmail && password == savedPassword) {
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()

                    // Log successful login
                    Log.d(TAG, "Login successful, navigating to MainActivity")

                    // Start MainActivity after successful login
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()  // Optionally finish this activity to prevent going back to login
                } else {
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()

                    // Log failed login attempt
                    Log.d(TAG, "Login failed: Incorrect email or password")
                }
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()

                // Log if fields are empty
                Log.d(TAG, "Please enter all fields")
            }
        }

        signUpTextView.setOnClickListener {
            // Log when navigating to the signup page
            Log.d(TAG, "Navigating to SignUpActivity")
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
