package com.example.mealmate.repository.impl

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mealmate.repository.AuthRepository
import com.example.mealmate.utils.LoadingUtil
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun registerUser(email: String, password: String, name: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                val userMap = mapOf(
                    "userId" to user.uid,
                    "name" to name,
                    "email" to email
                )
                firebaseDatabase.reference.child("users").child(user.uid).setValue(userMap).await()
            }
            result.user
        } catch (e: FirebaseAuthException) {
            throw Exception(getFirebaseErrorMessage(e))
        } catch (e: Exception) {
            throw Exception("Registration failed. Please try again.")
        }
    }

    private fun getFirebaseErrorMessage(e: FirebaseAuthException): String {
        return when (e.errorCode) {
            "ERROR_INVALID_EMAIL" -> "Invalid email format."
            "ERROR_WEAK_PASSWORD" -> "Password is too weak. Use at least 6 characters."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered."
            else -> "Authentication failed. Please check your details."
        }
    }



    override suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            throw Exception("Wrong credentials")

        }
    }


    override fun logoutUser() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}
