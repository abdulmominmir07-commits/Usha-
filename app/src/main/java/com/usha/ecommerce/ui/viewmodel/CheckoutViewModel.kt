package com.usha.ecommerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usha.ecommerce.data.model.CartItem
import com.usha.ecommerce.data.model.Order
import com.usha.ecommerce.data.repository.OrderRepository
import com.usha.ecommerce.data.repository.UserRepository
import com.usha.ecommerce.network.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {
    private val orderRepository = OrderRepository()
    private val paymentRepository = PaymentRepository()
    private val userRepository = UserRepository()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _orderCreated = MutableStateFlow(false)
    val orderCreated: StateFlow<Boolean> = _orderCreated.asStateFlow()
    
    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl.asStateFlow()
    
    private val _paymentInProgress = MutableStateFlow(false)
    val paymentInProgress: StateFlow<Boolean> = _paymentInProgress.asStateFlow()

    fun createOrder(
        items: List<CartItem>,
        totalPrice: Double,
        deliveryAddress: String,
        phone: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val currentUser = userRepository.getCurrentUser()
            if (currentUser == null) {
                _error.value = "ব্যবহারকারী লগইন করেননি"
                _isLoading.value = false
                return@launch
            }
            
            val order = Order(
                userId = currentUser.id,
                items = items,
                totalPrice = totalPrice,
                deliveryAddress = deliveryAddress,
                phone = phone,
                status = "Pending"
            )
            
            val result = orderRepository.createOrder(order)
            result.onSuccess { orderId ->
                _orderCreated.value = true
                initiatePayment(totalPrice, orderId, currentUser.email)
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "অর্ডার তৈরিতে ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    private fun initiatePayment(amount: Double, orderId: String, email: String) {
        viewModelScope.launch {
            _paymentInProgress.value = true
            _error.value = null
            
            val result = paymentRepository.initiatePayment(
                amount = amount.toString(),
                orderId = orderId,
                customerEmail = email,
                callbackUrl = "https://yourapp.com/payment/callback"
            )
            
            result.onSuccess { response ->
                _paymentUrl.value = response.bkashURL
                _paymentInProgress.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "পেমেন্ট শুরু করতে ব্যর্থ হয়েছে"
                _paymentInProgress.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
