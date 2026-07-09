package com.usha.ecommerce.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.usha.ecommerce.data.model.Order
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    private val ordersCollection = "orders"

    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val documentRef = db.collection(ordersCollection).add(order).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> {
        return try {
            db.collection(ordersCollection)
                .document(orderId)
                .update("status", status, "updatedAt", System.currentTimeMillis())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserOrders(userId: String): Result<List<Order>> {
        return try {
            val querySnapshot = db.collection(ordersCollection)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Order::class.java)?.copy(id = document.id)
            }
            
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrderById(orderId: String): Result<Order> {
        return try {
            val document = db.collection(ordersCollection)
                .document(orderId)
                .get()
                .await()
            
            val order = document.toObject(Order::class.java)?.copy(id = document.id)
            if (order != null) {
                Result.success(order)
            } else {
                Result.failure(Exception("অর্ডার পাওয়া যায়নি"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllOrders(): Result<List<Order>> {
        return try {
            val querySnapshot = db.collection(ordersCollection)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Order::class.java)?.copy(id = document.id)
            }
            
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
