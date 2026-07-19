package com.example.proyectotesismovil.data.remote.api

import com.example.proyectotesismovil.domain.model.Subscription
import com.example.proyectotesismovil.domain.model.SubscriptionInitResponse
import com.example.proyectotesismovil.domain.model.SubscriptionPlan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class CreateSubscriptionRequest(
    val plan_id: String
)

data class SubscriptionResponse(
    val subscription: Subscription?
)

interface PaymentApi {

    @GET("payments/plans")
    suspend fun getPlans(): Response<List<SubscriptionPlan>>

    @POST("payments/create-subscription")
    suspend fun createSubscription(
        @Body body: CreateSubscriptionRequest
    ): Response<SubscriptionInitResponse>

    @GET("payments/my-subscription")
    suspend fun getMySubscription(): Response<SubscriptionResponse>

    @POST("payments/cancel-subscription")
    suspend fun cancelSubscription(): Response<Map<String, String>>
}
