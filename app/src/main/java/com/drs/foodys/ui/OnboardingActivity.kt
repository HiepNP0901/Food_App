package com.drs.foodys.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drs.foodys.databinding.ActivityOnboardingBinding
import com.drs.foodys.ui.auth.LoginActivity

class OnboardingActivity : AppCompatActivity() {
    private val binding: ActivityOnboardingBinding by lazy {
        ActivityOnboardingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.onboardingButtonNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
    }
}