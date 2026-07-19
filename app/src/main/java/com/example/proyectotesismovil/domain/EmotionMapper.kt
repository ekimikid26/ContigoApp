package com.example.proyectotesismovil.domain

object EmotionMapper {

    fun toValue(emotion: String): Float {
        return when (emotion.lowercase()) {
            "feliz" -> 7f
            "tranquilo" -> 6f
            "agotado" -> 4f
            "estresado" -> 3f
            "triste" -> 3f
            "enojado" -> 2f
            "ansioso" -> 1f
            else -> 4f
        }
    }

    fun toEmoji(emotion: String): String {
        return when (emotion.lowercase()) {
            "feliz" -> "😁"
            "tranquilo" -> "😊"
            "agotado" -> "😴"
            "estresado" -> "😓"
            "triste" -> "😔"
            "enojado" -> "😤"
            "ansioso" -> "😰"
            else -> "😊"
        }
    }

    fun toColor(emotion: String): Long {
        return when (emotion.lowercase()) {
            "feliz" -> 0xFF7EC8A4
            "tranquilo" -> 0xFF6B9FD4
            "agotado" -> 0xFFB0BEC5
            "estresado" -> 0xFFFFB74D
            "triste" -> 0xFF90CAF9
            "enojado" -> 0xFFEF9A9A
            "ansioso" -> 0xFFA89BC8
            else -> 0xFF6B9FD4
        }
    }

    fun toLabel(emotion: String): String {
        return emotion.replaceFirstChar { it.uppercase() }
    }
}
