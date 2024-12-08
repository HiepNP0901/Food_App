package com.example.foodapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.foodapp.data.AuthResponsible
import com.example.foodapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val username= binding.username
        val password= binding.password
        editTextChange(username, password)

        binding.loginButton.setOnClickListener{
            if(username.text.toString().isEmpty()){
                username.error="This field cannot be empty"
                username.requestFocus()
            }
            else if(password.text.toString().isEmpty()){
                password.error="This field cannot be empty"
                password.requestFocus()
            }
            else if(username.error == null && password.error == null){
                AuthResponsible().login(username.text.toString(), password.text.toString()){
                    result -> when{
                        result.isSuccess -> {
                            Toast.makeText(this, result.getOrNull(), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        result.isFailure -> {
                            Toast.makeText(
                                this,
                                result.exceptionOrNull()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.dontHaveAccountButton.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun editTextChange(username: EditText, password: EditText){
        binding.username.doAfterTextChanged {
            if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
                username.error="Please enter a valid email or phone number"
                username.requestFocus()
            }
            else {
                username.error= null
                username.requestFocus()
            }
        }

        binding.password.doAfterTextChanged {
            if(password.text.toString().length < 6){
                password.error="Password must be at least 6 characters"
                password.requestFocus()
            }
            else {
                password.error= null
                password.requestFocus()
            }
        }
    }
}