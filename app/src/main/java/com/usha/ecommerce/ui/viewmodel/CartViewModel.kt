package com.usha.ecommerce.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.usha.ecommerce.data.local.CartManager
import com.usha.ecommerce.data.model.CartItem
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val cartManager = CartManager()
    
    val cartItems: StateFlow<List<CartItem>> = cartManager.cartItems

    fun addToCart(item: CartItem) {
        cartManager.addToCart(item)
    }

    fun removeFromCart(productId: String) {
        cartManager.removeFromCart(productId)
    }

    fun updateQuantity(productId: String, quantity: Int) {
        cartManager.updateQuantity(productId, quantity)
    }

    fun getTotalPrice(): Double {
        return cartManager.getTotalPrice()
    }

    fun getCartItemCount(): Int {
        return cartManager.getCartItemCount()
    }

    fun clearCart() {
        cartManager.clearCart()
    }
}
