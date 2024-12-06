package com.example.foodapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.data.AuthResponsible
import com.example.foodapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.logout.setOnClickListener {
            AuthResponsible().logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}