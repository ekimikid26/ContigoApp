package com.example.proyectotesismovil.domain.model

import com.google.gson.annotations.SerializedName

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val amount: Int,
    val currency: String,
    val description: String,
    val interval: String,
    val features: List<String> = emptyList()
) {
    // El backend ya manda "amount" en pesos MXN completos (no centavos)
    val formattedPrice: String
        get() = "$${amount}.00 MXN/mes"
}

data class Subscription(
    val id: Int,
    @SerializedName("plan_id") val planId: String,
    @SerializedName("plan_name") val planName: String,
    val status: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String?,
    val amount: Int
)

data class SubscriptionInitResponse(
    @SerializedName("init_point") val initPoint: String,
    @SerializedName("preapproval_id") val preapprovalId: String
)
