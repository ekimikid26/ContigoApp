package com.example.proyectotesismovil.data.repository

import com.example.proyectotesismovil.data.remote.api.CreateSubscriptionRequest
import com.example.proyectotesismovil.data.remote.api.PaymentApi
import com.example.proyectotesismovil.domain.model.Subscription
import com.example.proyectotesismovil.domain.model.SubscriptionInitResponse
import com.example.proyectotesismovil.domain.model.SubscriptionPlan

class PaymentRepositoryImpl(private val paymentApi: PaymentApi) {

    suspend fun getPlans(): Result<List<SubscriptionPlan>> {
        return try {
            val response = paymentApi.getPlans()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("No se pudieron cargar los planes")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Sin conexión. Verifica tu red.")
            )
        }
    }

    suspend fun createSubscription(
        planId: String
    ): Result<SubscriptionInitResponse> {
        return try {
            val response = paymentApi.createSubscription(
                CreateSubscriptionRequest(planId)
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val code = response.code()
                when (code) {
                    400 -> Result.failure(
                        Exception("Ya tienes una suscripción activa.")
                    )
                    else -> Result.failure(
                        Exception("Error al iniciar la suscripción ($code).")
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Sin conexión. Verifica tu red.")
            )
        }
    }

    suspend fun getMySubscription(): Result<Subscription?> {
        return try {
            val response = paymentApi.getMySubscription()
            if (response.isSuccessful) {
                Result.success(response.body()?.subscription)
            } else {
                Result.failure(Exception("Error al consultar suscripción"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión."))
        }
    }

    suspend fun cancelSubscription(): Result<String> {
        return try {
            val response = paymentApi.cancelSubscription()
            if (response.isSuccessful) {
                Result.success("Suscripción cancelada exitosamente")
            } else {
                Result.failure(Exception("No se pudo cancelar."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión."))
        }
    }
}
