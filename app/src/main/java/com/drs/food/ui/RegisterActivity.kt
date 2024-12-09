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
import com.drs.food.databinding.ActivityRegisterBinding
import com.drs.food.service.AuthService

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var name: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var filter: IntentFilter
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        name = binding.name
        username = binding.username
        password = binding.password
        filter = IntentFilter(AuthService.RESULT)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                binding.createAccount.isEnabled = true
                binding.progressBar.visibility = View.GONE
                intent?.getStringExtra("nextActivity")?.let {
                    startActivity(Intent(this@RegisterActivity, Class.forName(it)))
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

    private fun setBindingButton() {
        binding.createAccount.setOnClickListener {
            if (name.text.toString().isEmpty()) {
                name.error = "This field cannot be empty"
                name.requestFocus()
            } else if (username.text.toString().isEmpty()) {
                username.error = "This field cannot be empty"
                username.requestFocus()
            } else if (password.text.toString().isEmpty()) {
                password.error = "This field cannot be empty"
                password.requestFocus()
            } else if (name.error == null && username.error == null && password.error == null) {
                binding.createAccount.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                startService(Intent(this, AuthService::class.java).apply {
                    action = AuthService.ACTION_REGISTER
                    putExtra(AuthService.EXTRA_NAME, name.text.toString())
                    putExtra(AuthService.EXTRA_USERNAME, username.text.toString())
                    putExtra(AuthService.EXTRA_PASSWORD, password.text.toString())
                })
            }
        }

        binding.alreadyHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    private fun setBindingEditText() {
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
            if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
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
    }
}