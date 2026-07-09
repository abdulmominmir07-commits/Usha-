package com.usha.ecommerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usha.ecommerce.data.model.Order
import com.usha.ecommerce.data.repository.OrderRepository
import com.usha.ecommerce.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel : ViewModel() {
    private val orderRepository = OrderRepository()
    private val userRepository = UserRepository()
    
    private val _userOrders = MutableStateFlow<List<Order>>(emptyList())
    val userOrders: StateFlow<List<Order>> = _userOrders.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadUserOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val userId = userRepository.getCurrentUserId()
            if (userId == null) {
                _error.value = "ব্যবহারকারী লগইন করেননি"
                _isLoading.value = false
                return@launch
            }
            
            val result = orderRepository.getUserOrders(userId)
            result.onSuccess { orders ->
                _userOrders.value = orders
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "অর্ডার লোড করতে ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
