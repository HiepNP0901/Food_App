package com.example.foodapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates

//Splash Screen sẽ hiển thị trong 3s
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var isFirstTime by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        isFirstTime = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstTime", true)
        val nextActivity = if (isFirstTime) OnboardingActivity::class.java else LoginActivity::class.java
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, nextActivity))
                finish()
            }, 3000
        )
    }
}