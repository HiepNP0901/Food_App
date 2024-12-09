package com.drs.food.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.drs.food.databinding.ActivityLoginBinding
import com.drs.food.service.AuthService

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var filter: IntentFilter
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        username = binding.username
        password= binding.password
        filter = IntentFilter(AuthService.RESULT)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                binding.loginButton.isEnabled = true
                binding.progressBar.visibility = View.GONE
                intent?.getStringExtra("nextActivity")?.let {
                    startActivity(Intent(this@LoginActivity, Class.forName(it)))
                    finishAffinity()
                }
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        setBindingButton()
        setBindingEditText()
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun setBindingButton() {
        binding.loginButton.setOnClickListener {
            if (username.text.toString().isEmpty()) {
                username.error = "This field cannot be empty"
                username.requestFocus()
            } else if (password.text.toString().isEmpty()) {
                password.error = "This field cannot be empty"
                password.requestFocus()
            } else if (username.error == null && password.error == null) {
                binding.loginButton.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                startService(Intent(this, AuthService::class.java).apply {
                    action = AuthService.ACTION_LOGIN
                    putExtra(AuthService.EXTRA_USERNAME, username.text.toString())
                    putExtra(AuthService.EXTRA_PASSWORD, password.text.toString())
                })
            }
        }

        binding.dontHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setBindingEditText(){
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