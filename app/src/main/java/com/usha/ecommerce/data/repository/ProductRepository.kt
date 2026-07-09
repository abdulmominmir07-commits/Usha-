package com.usha.ecommerce.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.usha.ecommerce.data.model.Product
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val db = FirebaseFirestore.getInstance()
    private val productsCollection = "products"

    suspend fun addProduct(product: Product): Result<String> {
        return try {
            val documentRef = db.collection(productsCollection).add(product).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(productId: String, product: Product): Result<Unit> {
        return try {
            db.collection(productsCollection)
                .document(productId)
                .set(product)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            db.collection(productsCollection)
                .document(productId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val querySnapshot = db.collection(productsCollection)
                .get()
                .await()
            
            val products = querySnapshot.documents.mapNotNull { document ->
                val product = document.toObject(Product::class.java)
                product?.copy(id = document.id)
            }
            
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(productId: String): Result<Product> {
        return try {
            val document = db.collection(productsCollection)
                .document(productId)
                .get()
                .await()
            
            val product = document.toObject(Product::class.java)?.copy(id = document.id)
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("পণ্য পাওয়া যায়নি"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val querySnapshot = db.collection(productsCollection)
                .get()
                .await()
            
            val products = querySnapshot.documents.mapNotNull { document ->
                val product = document.toObject(Product::class.java)?.copy(id = document.id)
                product
            }.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)
            }
            
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val querySnapshot = db.collection(productsCollection)
                .whereEqualTo("category", category)
                .get()
                .await()
            
            val products = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Product::class.java)?.copy(id = document.id)
            }
            
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
