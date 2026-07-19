package com.example.proyectotesismovil.domain.usecase

import com.example.proyectotesismovil.domain.model.Recommendation

class GetRecommendationsUseCase {
    fun execute(riskLevel: Int): List<Recommendation> {
        return listOf(
            Recommendation(
                "Respiración",
                "3 min",
                "Tómate un momento para respirar profundamente.",
                "breath"
            ),
            Recommendation(
                "Descanso",
                "10 min",
                "Un breve descanso puede renovar tu energía.",
                "rest"
            ),
            Recommendation(
                "Música tranquila",
                "15 min",
                "Escucha algo que te relaje.",
                "music"
            ),
            Recommendation(
                "Reflexión",
                "5 min",
                "Piensa en algo positivo de tu día.",
                "reflect"
            )
        )
    }
}
