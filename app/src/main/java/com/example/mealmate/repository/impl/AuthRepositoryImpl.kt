package com.example.mealmate.repository.impl

import com.example.mealmate.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
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
                // Save user data to Firebase Realtime Database
                firebaseDatabase.reference.child("users").child(user.uid).setValue(userMap).await()
            }
            result.user
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun loginUser(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null // Handle error in ViewModel
        }
    }

    override fun logoutUser() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
