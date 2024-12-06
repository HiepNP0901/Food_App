package com.example.foodapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.foodapp.data.AuthResponsible
import com.example.foodapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val name = binding.name
        val username = binding.username
        val password = binding.password

        binding.name.doAfterTextChanged {
            if (name.text.toString().isEmpty()) {
                name.error = "This field cannot be empty"
                name.requestFocus()
            } else {
                name.error = null
                name.requestFocus()
            }
        }

        binding.username.doAfterTextChanged {
            if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()
            ) {
                username.error = "Please enter a valid email or phone number"
                username.requestFocus()
            } else {
                username.error = null
                username.requestFocus()
            }
        }

        binding.password.doAfterTextChanged {
            if (password.text.toString().length < 6) {
                password.error = "Password must be at least 6 characters"
                password.requestFocus()
            } else {
                password.error = null
                password.requestFocus()
            }
        }

        binding.createAccount.setOnClickListener {
            if (name.text.toString().isEmpty()) {
                name.error = "This field cannot be empty"
                name.requestFocus()
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()
                && !Patterns.PHONE.matcher(username.text.toString()).matches()
            ){
                username.error = "Please enter a valid email or phone number"
                username.requestFocus()
            }
            else if (password.text.toString().length < 6) {
                password.error = "Password must be at least 6 characters"
                password.requestFocus()
            }
            else if (username.text.toString().isEmpty()) {
                username.error = "This field cannot be empty"
                username.requestFocus()
            }
            else if (password.text.toString().isEmpty()) {
                password.error = "This field cannot be empty"
                password.requestFocus()
            }
            else if (name.error == null && username.error == null && password.error == null) {
                val result = AuthResponsible().register(
                    name.text.toString(),
                    username.text.toString(),
                    password.text.toString()
                )
                if (result.isSuccess) {
                    Toast.makeText(this, result.getOrNull(), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LocationActivity::class.java))
                    finish()
                }
                else {
                    Toast.makeText(this, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.alreadyHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}