package com.drs.foodys.ui.auth

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
import com.drs.foodys.R
import com.drs.foodys.databinding.ActivityRegisterBinding
import com.drs.foodys.service.AuthService
import com.drs.foodys.ui.auth.fragment.LogoFragment

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var name: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var filter: IntentFilter
    private lateinit var receiver: BroadcastReceiver
    private lateinit var fragment: LogoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view using the binding object
        setContentView(binding.root)

        // Initialize the fragment
        fragment = LogoFragment.setTitle(getString(R.string.register_title))
        supportFragmentManager.beginTransaction().add(binding.containerFragment.id, fragment).commit()

        // Initialize the views
        name = binding.name
        username = binding.username
        password = binding.password
        setBindingButton()
        setBindingEditText()

        // Initialize the receiver and filter
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
        registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    // Set the click listeners for the buttons
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

        binding.googleButton.setOnClickListener {
            startService(Intent(this, AuthService::class.java).apply {
                action = AuthService.GOOGLE_SIGN_IN
            })
        }

        binding.facebookButton.setOnClickListener {
            startService(Intent(this, AuthService::class.java).apply {
                action = AuthService.FACEBOOK_SIGN_IN
            })
        }

        binding.alreadyHaveAccountButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    // Set action do after text changed for the edit text
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