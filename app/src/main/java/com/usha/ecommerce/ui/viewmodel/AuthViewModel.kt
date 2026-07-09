package com.usha.ecommerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usha.ecommerce.data.model.User
import com.usha.ecommerce.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository = UserRepository()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        checkIfUserLoggedIn()
    }

    fun registerUser(email: String, password: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = userRepository.registerUser(email, password, name)
            result.onSuccess { user ->
                _currentUser.value = user
                _isLoggedIn.value = true
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "রেজিস্ট্রেশন ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = userRepository.loginUser(email, password)
            result.onSuccess { user ->
                _currentUser.value = user
                _isLoggedIn.value = true
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "লগইন ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    fun logoutUser() {
        userRepository.logoutUser()
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            if (userRepository.isUserLoggedIn()) {
                val user = userRepository.getCurrentUser()
                _currentUser.value = user
                _isLoggedIn.value = true
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
