package com.usha.ecommerce.network

import android.util.Base64
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PaymentRepository {
    private val baseUrl = "https://checkout.sandbox.bkash.com/" // Sandbox URL
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val bkashApiService = retrofit.create(BkashApiService::class.java)

    // আপনার bKash API credentials
    private val appKey = "YOUR_APP_KEY"
    private val appSecret = "YOUR_APP_SECRET"
    private val username = "YOUR_USERNAME"
    private val password = "YOUR_PASSWORD"

    suspend fun initiatePayment(
        amount: String,
        orderId: String,
        customerEmail: String,
        callbackUrl: String
    ): Result<BkashPaymentResponse> {
        return try {
            val authorization = getAuthorization()
            val request = BkashPaymentRequest(
                mode = "0011",
                payerReference = customerEmail,
                callbackURL = callbackUrl,
                amount = amount,
                currency = "BDT",
                intent = "sale",
                merchantInvoiceNumber = orderId
            )

            val response = bkashApiService.createPayment(
                authorization = authorization,
                appKey = appKey,
                request = request
            )

            if (response.statusCode == "0000") {
                Result.success(response)
            } else {
                Result.failure(Exception("পেমেন্ট শুরু করতে ব্যর্থ: ${response.statusMessage}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun executePayment(paymentId: String): Result<BkashExecuteResponse> {
        return try {
            val authorization = getAuthorization()
            val request = BkashExecuteRequest(paymentID = paymentId)

            val response = bkashApiService.executePayment(
                authorization = authorization,
                appKey = appKey,
                request = request
            )

            if (response.statusCode == "0000") {
                Result.success(response)
            } else {
                Result.failure(Exception("পেমেন্ট সম্পন্ন করতে ব্যর্থ: ${response.statusMessage}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getAuthorization(): String {
        val credentials = "$username:$password"
        val encodedCredentials = Base64.encodeToString(
            credentials.toByteArray(),
            Base64.NO_WRAP
        )
        return "Basic $encodedCredentials"
    }
}
