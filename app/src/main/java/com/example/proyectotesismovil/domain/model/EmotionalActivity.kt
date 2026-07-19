package com.example.proyectotesismovil.domain.model

data class EmotionalActivity(
    val title: String,
    val duration: String,
    val description: String,
    val iconType: String,
    val route: String,
    val isRecommended: Boolean = false
)
