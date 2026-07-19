package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.local.dao.ActivityLogDao
import com.example.proyectotesismovil.data.local.dao.EmotionalStateDao
import com.example.proyectotesismovil.domain.EmotionMapper
import com.example.proyectotesismovil.ui.state.ActivitySummary
import com.example.proyectotesismovil.ui.state.DailyEmotionPoint
import com.example.proyectotesismovil.ui.state.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HistoryViewModel(
    private val emotionalStateDao: EmotionalStateDao,
    private val activityLogDao: ActivityLogDao,
    private val userId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val sevenDaysAgo: Long
        get() = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                emotionalStateDao
                    .getLastSevenDays(userId, sevenDaysAgo)
                    .collectLatest { states ->
                        val weeklyPoints = buildWeeklyPoints(states)

                        val mostFrequent = emotionalStateDao
                            .getMostFrequentEmotion(userId, sevenDaysAgo)

                        val totalRegs = emotionalStateDao
                            .getTotalRegistrations(userId, sevenDaysAgo)

                        val mostUsed = activityLogDao
                            .getMostUsedActivity(userId, sevenDaysAgo)

                        val activeDays = activityLogDao
                            .getActiveDays(userId, sevenDaysAgo)

                        activityLogDao
                            .getRecentActivities(userId, 10)
                            .collectLatest { activities ->
                                val activitySummaries = activities.map {
                                    ActivitySummary(
                                        activityType = it.activityType,
                                        completedAt = it.completedAt,
                                        durationSeconds = it.durationSeconds,
                                        activityIcon = mapActivityIcon(
                                            it.activityType
                                        ),
                                        activityLabel = mapActivityLabel(
                                            it.activityType
                                        )
                                    )
                                }

                                _uiState.value = HistoryUiState(
                                    isLoading = false,
                                    weeklyEmotions = weeklyPoints,
                                    recentActivities = activitySummaries,
                                    mostFrequentEmotion = mostFrequent,
                                    mostUsedActivity = mostUsed,
                                    totalRegistrations = totalRegs,
                                    activeDays = activeDays
                                )
                            }
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "No pudimos cargar tu historial. Intenta de nuevo."
                )
            }
        }
    }

    private fun buildWeeklyPoints(
        states: List<com.example.proyectotesismovil.data.local.entity.EmotionalStateEntity>
    ): List<DailyEmotionPoint> {
        val dayFormat = SimpleDateFormat("EEE", Locale("es", "MX"))
        val calendar = Calendar.getInstance()

        val last7Days = (6 downTo 0).map { daysAgo ->
            val tempCal = Calendar.getInstance()
            tempCal.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val startOfDay = tempCal.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val endOfDay = startOfDay + (24 * 60 * 60 * 1000L) - 1
            Pair(startOfDay, endOfDay)
        }

        return last7Days.map { (start, end) ->
            val dayStates = states.filter {
                it.registeredAt in start..end
            }

            val avgValue = if (dayStates.isNotEmpty()) {
                dayStates.map {
                    EmotionMapper.toValue(it.emotionalState)
                }.average().toFloat()
            } else 0f

            val dominantEmotion = dayStates
                .groupBy { it.emotionalState }
                .maxByOrNull { it.value.size }
                ?.key ?: ""

            calendar.timeInMillis = start
            DailyEmotionPoint(
                dayLabel = dayFormat.format(calendar.time)
                    .replaceFirstChar { it.uppercase() },
                emotionValue = avgValue,
                emotionName = dominantEmotion,
                date = start
            )
        }
    }

    private fun mapActivityIcon(activityType: String): String {
        return when (activityType.lowercase()) {
            "respiración" -> "🫁"
            "descanso" -> "😴"
            "música" -> "🎵"
            "reflexión" -> "📝"
            "gratitud" -> "🙏"
            "grounding" -> "🌿"
            "visualización" -> "🌅"
            "estiramiento" -> "🧘"
            "movimiento" -> "🏃"
            "escritura de descarga" -> "✍️"
            "prioridades" -> "📋"
            else -> "💙"
        }
    }

    private fun mapActivityLabel(activityType: String): String {
        return activityType.replaceFirstChar { it.uppercase() }
    }

    fun retry() {
        loadHistory()
    }
}
