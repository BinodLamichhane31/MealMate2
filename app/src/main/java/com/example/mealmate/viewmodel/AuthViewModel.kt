package com.example.mealmate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmate.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun register(email: String, password: String,name: String) {
        viewModelScope.launch {
            val newUser = repository.registerUser(email, password, name)
            if (newUser != null) {
                _user.value = newUser
            } else {
                _error.value = "Registration failed"
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val loginUser = repository.loginUser(email, password)
            if (loginUser != null) {
                _user.value = loginUser
            } else {
                _error.value = "Login failed"
            }
        }
    }

    fun logout() {
        repository.logoutUser()
        _user.value = null
    }
}