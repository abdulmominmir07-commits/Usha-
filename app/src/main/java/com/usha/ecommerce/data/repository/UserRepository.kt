package com.usha.ecommerce.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.usha.ecommerce.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = "users"

    suspend fun registerUser(email: String, password: String, name: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
                    name = name,
                    email = email,
                    createdAt = System.currentTimeMillis()
                )
                
                db.collection(usersCollection)
                    .document(firebaseUser.uid)
                    .set(user)
                    .await()
                
                Result.success(user)
            } else {
                Result.failure(Exception("রেজিস্ট্রেশন ব্যর্থ হয়েছে"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = db.collection(usersCollection)
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                    .toObject(User::class.java)
                
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("ব্যবহারকারী তথ্য পাওয়া যায়নি"))
                }
            } else {
                Result.failure(Exception("লগইন ব্যর্থ হয়েছে"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        return try {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                db.collection(usersCollection)
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                    .toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
