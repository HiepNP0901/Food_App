package com.drs.food.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import com.drs.food.ui.LoginActivity
import com.drs.food.ui.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private val auth = Firebase.auth

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            val intent = msg.obj as Intent
            when (intent.action) {
                ACTION_LOGIN -> handleLogin(intent)
                ACTION_REGISTER -> handleRegister(intent)
                ACTION_LOGOUT -> handleLogout()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        HandlerThread("AuthServiceThread", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            msg.obj = intent
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
    }

    private fun handleLogin(intent: Intent?) {
        val username = intent?.getStringExtra(EXTRA_USERNAME) ?: ""
        val password = intent?.getStringExtra(EXTRA_PASSWORD) ?: ""
        val email = if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if (currentUser?.isEmailVerified == false) {
                    sendResult("Please verify your email")
                } else {
                    sendResult("Login Successful", MainActivity::class.java)
                }
            }
            .addOnFailureListener { exception ->
                sendResult(exception.message ?: "Login Failed")
            }
    }

    private fun handleRegister(intent: Intent?) {
        val name = intent?.getStringExtra(EXTRA_NAME) ?: ""
        val username = intent?.getStringExtra(EXTRA_USERNAME) ?: ""
        val password = intent?.getStringExtra(EXTRA_PASSWORD) ?: ""
        val email = if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                currentUser?.sendEmailVerification()
                sendResult("Hello $name, please check your email to verify your account", LoginActivity::class.java)
                auth.signOut()
            }
            .addOnFailureListener { exception ->
                sendResult(exception.message ?: "Registration Failed")
            }
    }

    private fun handleLogout() {
        auth.signOut()
        sendResult("Logout Successful", LoginActivity::class.java)
    }

    private fun sendResult(message: String, nextActivity: Class<*>? = null) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        val intent = Intent(RESULT).apply {
            putExtra("nextActivity", nextActivity?.name)
        }
        sendBroadcast(intent)
    }

    companion object {
        const val ACTION_LOGIN = "com.drs.food.LOGIN"
        const val ACTION_REGISTER = "com.drs.food.REGISTER"
        const val ACTION_LOGOUT = "com.drs.food.LOGOUT"
        const val RESULT = "com.drs.food.auth.RESULT"

        const val EXTRA_USERNAME = "username"
        const val EXTRA_PASSWORD = "password"
        const val EXTRA_NAME = "name"
    }
}
