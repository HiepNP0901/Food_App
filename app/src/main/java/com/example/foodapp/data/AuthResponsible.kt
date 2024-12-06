package com.example.foodapp.data

import android.util.Patterns
import com.example.foodapp.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

class AuthResponsible {
    private val usersRef = FirebaseAuth.getInstance()

    fun login(username: String, password: String): Result<String> {
        return try {
            when {
                username == "admin@gmail.com" && password == "123456" -> Result.success("Login Successful")
                else -> Result.failure(IOException("Email/phone number or password is incorrect"))
            }
        } catch (e: Exception) {
            Result.failure(IOException("Error logging in", e))
        }
    }
    fun register(name: String, username: String, password: String): Result<String>{
         try {
            val email = if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) username else ""
            val phone = if (Patterns.PHONE.matcher(username).matches()) username else ""
//            usersRef.createUserWithEmailAndPassword(email, password)
            return Result.success("Registration Successful")
        } catch (e: Exception){
            return Result.failure(IOException("Error registering", e))
        }
    }

    fun logout(){
        usersRef.signOut()
    }
}