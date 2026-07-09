package com.usha.ecommerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usha.ecommerce.data.model.Product
import com.usha.ecommerce.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = productRepository.getAllProducts()
            result.onSuccess { products ->
                _products.value = products
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "পণ্য লোড করতে ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        
        if (query.isEmpty()) {
            loadProducts()
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = productRepository.searchProducts(query)
            result.onSuccess { products ->
                _products.value = products
                _isLoading.value = false
            }
            result.onFailure { exception ->
                _error.value = exception.message ?: "অনুসন্ধান ব্যর্থ হয়েছে"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
