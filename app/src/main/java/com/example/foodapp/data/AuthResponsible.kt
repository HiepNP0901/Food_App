package com.example.foodapp.data

import android.util.Patterns
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.io.IOException

class AuthResponsible {
    private val auth = Firebase.auth

    fun login(username: String, password: String, callback: (Result<String>) -> Unit){
        val email = if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
        //val phone = if (Patterns.PHONE.matcher(username).matches()) username else ""
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if (currentUser?.isEmailVerified == false) {
                    callback(Result.failure(IOException("Please verify your email")))
                } else{
                    callback(Result.success("Login Successful"))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(IOException(exception.message)))
            }
    }

    fun register(name: String, username: String, password: String, callback: (Result<String>) -> Unit){
        val email = if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
        //val phone = if (Patterns.PHONE.matcher(username).matches()) username else ""
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                currentUser?.sendEmailVerification()
                callback(Result.success("Hello $name, please check your email to verify your account"))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(IOException(exception.message)))
            }
    }

    fun logout(){
        auth.signOut()
    }
}