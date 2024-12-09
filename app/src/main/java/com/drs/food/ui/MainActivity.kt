package com.drs.food.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drs.food.databinding.ActivityMainBinding
import com.drs.food.service.AuthService

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var filter: IntentFilter
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        filter = IntentFilter(AuthService.RESULT)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.getStringExtra("nextActivity")?.let {
                    startActivity(Intent(this@MainActivity, Class.forName(it)))
                    finishAffinity()
                }
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, filter)
        binding.logout.setOnClickListener {
            startService(Intent(this, AuthService::class.java).apply {
                action = AuthService.ACTION_LOGOUT
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}