package com.example.mealmate.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun registerUser(email: String, password: String,name: String): FirebaseUser?
    suspend fun loginUser(email: String, password: String): FirebaseUser?
    fun logoutUser()
    fun getCurrentUser(): FirebaseUser?
}