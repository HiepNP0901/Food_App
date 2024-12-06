package com.example.foodapp.data

import android.util.Patterns
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.io.IOException

class AuthResponsible {
    private val auth = Firebase.auth

    fun login(username: String, password: String): Result<String> {
        val email = if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
        //val phone = if (Patterns.PHONE.matcher(username).matches()) username else ""
        var result: Result<String> = Result.failure(IOException("Login Failed"))
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                result = if (currentUser?.isEmailVerified == false) {
                    Result.failure(IOException("Please verify your email"))
                } else{
                    Result.success("Login Successful")
                }
            }
            .addOnFailureListener { exception ->
                result = Result.failure(IOException("Login Failed", exception))
            }
        return result
    }

    fun register(name: String, username: String, password: String): String {
        val email = if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
        //val phone = if (Patterns.PHONE.matcher(username).matches()) username else ""
        //TODO: send verification code to email or phone number
        auth.createUserWithEmailAndPassword(email, password)
        return "Hello $name, your account has been created successfully"
    }

    fun logout(){
        auth.signOut()
    }
}