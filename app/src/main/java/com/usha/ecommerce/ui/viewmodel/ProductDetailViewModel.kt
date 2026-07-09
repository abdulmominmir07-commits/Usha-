package com.usha.ecommerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usha.ecommerce.data.model.Product
import com.usha.ecommerce.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = productRepository.getProductById(productId)
            result.onSuccess { product ->
                _product.value = product
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "পণ্য লোড করতে ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    fun increaseQuantity() {
        _quantity.value++
    }

    fun decreaseQuantity() {
        if (_quantity.value > 1) {
            _quantity.value--
        }
    }

    fun setQuantity(qty: Int) {
        if (qty > 0) {
            _quantity.value = qty
        }
    }

    fun clearError() {
        _error.value = null
    }
}
