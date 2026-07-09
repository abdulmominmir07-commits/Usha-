package com.usha.ecommerce.data.local

import com.usha.ecommerce.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartManager {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(item: CartItem) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.productId == item.productId }
        
        if (existingItem != null) {
            // পণ্য ইতিমধ্যে আছে, পরিমাণ বৃদ্ধি করুন
            val index = currentCart.indexOf(existingItem)
            currentCart[index] = existingItem.copy(quantity = existingItem.quantity + item.quantity)
        } else {
            // নতুন পণ্য যোগ করুন
            currentCart.add(item)
        }
        
        _cartItems.value = currentCart
    }

    fun removeFromCart(productId: String) {
        val currentCart = _cartItems.value.toMutableList()
        currentCart.removeAll { it.productId == productId }
        _cartItems.value = currentCart
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val currentCart = _cartItems.value.toMutableList()
        val itemIndex = currentCart.indexOfFirst { it.productId == productId }
        
        if (itemIndex != -1) {
            if (quantity > 0) {
                currentCart[itemIndex] = currentCart[itemIndex].copy(quantity = quantity)
            } else {
                currentCart.removeAt(itemIndex)
            }
            _cartItems.value = currentCart
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.getTotalPrice() }
    }

    fun getCartItemCount(): Int {
        return _cartItems.value.size
    }
}
