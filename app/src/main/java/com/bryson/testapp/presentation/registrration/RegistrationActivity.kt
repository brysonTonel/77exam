package com.bryson.testapp.presentation.registrration

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bryson.testapp.R
import com.bryson.testapp.presentation.states.RegistrationState
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {

    private val viewModel: RegistrationViewModel by viewModel()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val usernameField = findViewById<EditText>(R.id.usernameRegister)
        val passwordField = findViewById<EditText>(R.id.passwordRegister)
        val loginButton = findViewById<Button>(R.id.submitButton)


        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            viewModel.register(username, password)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registrationState.observe(this) { state ->
            when (state) {
                is RegistrationState.Success -> {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }

                is RegistrationState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}