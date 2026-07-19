package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.local.entity.EmotionalStateEntity
import com.example.proyectotesismovil.domain.model.Emotion
import com.example.proyectotesismovil.domain.model.EmotionalActivity
import com.example.proyectotesismovil.domain.repository.EmotionalActivityRepository
import com.example.proyectotesismovil.ui.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ForYouViewModel(
    private val repository: EmotionalActivityRepository
) : ViewModel() {

    private val _selectedEmotion = MutableStateFlow<Emotion?>(null)
    val selectedEmotion: StateFlow<Emotion?> = _selectedEmotion.asStateFlow()

    private val _activities = MutableStateFlow<List<EmotionalActivity>>(emptyList())
    val activities: StateFlow<List<EmotionalActivity>> = _activities.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLastEmotionalState().collect { entity ->
                if (entity != null) {
                    try {
                        val emotion = Emotion.valueOf(entity.emotionalState)
                        _selectedEmotion.value = emotion
                        updateActivities(emotion)
                    } catch (e: Exception) {
                        // Ignore if invalid
                    }
                }
            }
        }
    }

    fun selectEmotion(emotion: Emotion) {
        _selectedEmotion.value = emotion
        updateActivities(emotion)
        viewModelScope.launch {
            repository.saveEmotionalState(
                EmotionalStateEntity(
                    emotionalState = emotion.name,
                    registeredAt = System.currentTimeMillis()
                )
            )
        }
    }

    private fun updateActivities(emotion: Emotion) {
        _activities.value = when (emotion) {
            Emotion.ANSIOSO -> listOf(
                EmotionalActivity("Respiración", "3 min", "Inhala y exhala con calma.", "breath", Screen.Breathing.route, true),
                EmotionalActivity("Técnica 5-4-3-2-1", "5 min", "Conecta nuevamente con tu entorno.", "grounding", Screen.Grounding.route),
                EmotionalActivity("Música relajante", "15 min", "Melodías suaves para este momento.", "music", Screen.Music.route),
                EmotionalActivity("Visualización guiada", "10 min", "Imagina un lugar seguro y tranquilo.", "visualization", Screen.Visualization.route)
            )
            Emotion.ENOJADO -> listOf(
                EmotionalActivity("Respiración", "3 min", "Dale espacio a tu respiración.", "breath", Screen.Breathing.route, true),
                EmotionalActivity("Movimiento rápido", "5 min", "Libera tensión con pequeños movimientos.", "quick_move", Screen.QuickMovement.route),
                EmotionalActivity("Escritura de descarga", "10 min", "Escribe sin filtros lo que piensas.", "release_writing", Screen.ReleaseWriting.route),
                EmotionalActivity("Música relajante", "15 min", "Permite que el sonido te acompañe.", "music", Screen.Music.route)
            )
            Emotion.TRISTE -> listOf(
                EmotionalActivity("Música relajante", "15 min", "Permítete sentir acompañado.", "music", Screen.Music.route, true),
                EmotionalActivity("Reflexión personal", "5 min", "Escribe lo que llevas dentro.", "reflection", Screen.Reflection.route),
                EmotionalActivity("Gratitud", "5 min", "Recuerda pequeños momentos positivos.", "gratitude", Screen.Gratitude.route),
                EmotionalActivity("Movimiento suave", "10 min", "Estira tu cuerpo lentamente.", "stretching", Screen.Stretching.route)
            )
            Emotion.ESTRESADO -> listOf(
                EmotionalActivity("Descanso guiado", "10 min", "Haz una pausa por unos minutos.", "rest", Screen.Rest.route, true),
                EmotionalActivity("Respiración", "3 min", "Reduce la velocidad poco a poco.", "breath", Screen.Breathing.route),
                EmotionalActivity("Lista de prioridades", "5 min", "Organiza tus pensamientos.", "priority", Screen.Priority.route),
                EmotionalActivity("Música relajante", "15 min", "Desconecta por un momento.", "music", Screen.Music.route)
            )
            Emotion.AGOTADO -> listOf(
                EmotionalActivity("Descanso guiado", "10 min", "Tu cuerpo también necesita pausas.", "rest", Screen.Rest.route, true),
                EmotionalActivity("Música relajante", "15 min", "Relájate sin presión.", "music", Screen.Music.route),
                EmotionalActivity("Estiramiento guiado", "10 min", "Muévete lentamente.", "stretching", Screen.Stretching.route),
                EmotionalActivity("Visualización guiada", "10 min", "Respira e imagina tranquilidad.", "visualization", Screen.Visualization.route)
            )
            Emotion.TRANQUILO -> listOf(
                EmotionalActivity("Reflexión personal", "5 min", "Aprovecha este momento de calma.", "reflection", Screen.Reflection.route, true),
                EmotionalActivity("Gratitud", "5 min", "Escribe algo bueno de hoy.", "gratitude", Screen.Gratitude.route),
                EmotionalActivity("Música relajante", "15 min", "Mantén esta sensación agradable.", "music", Screen.Music.route),
                EmotionalActivity("Respiración", "3 min", "Conecta contigo mismo.", "breath", Screen.Breathing.route)
            )
            Emotion.FELIZ -> listOf(
                EmotionalActivity("Reflexión personal", "5 min", "Guarda este buen momento.", "reflection", Screen.Reflection.route, true),
                EmotionalActivity("Gratitud", "5 min", "Comparte contigo algo positivo.", "gratitude", Screen.Gratitude.route),
                EmotionalActivity("Música relajante", "15 min", "Disfruta este estado emocional.", "music", Screen.Music.route),
                EmotionalActivity("Movimiento suave", "10 min", "Activa tu cuerpo con calma.", "stretching", Screen.Stretching.route)
            )
        }
    }
}
