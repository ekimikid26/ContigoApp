package com.example.proyectotesismovil.ui.state

data class HistoryUiState(
    val isLoading: Boolean = true,
    val weeklyEmotions: List<DailyEmotionPoint> = emptyList(),
    val recentActivities: List<ActivitySummary> = emptyList(),
    val mostFrequentEmotion: String? = null,
    val mostUsedActivity: String? = null,
    val totalRegistrations: Int = 0,
    val activeDays: Int = 0,
    val error: String? = null
)

data class DailyEmotionPoint(
    val dayLabel: String,
    val emotionValue: Float,
    val emotionName: String,
    val date: Long
)

data class ActivitySummary(
    val activityType: String,
    val completedAt: Long,
    val durationSeconds: Int,
    val activityIcon: String,
    val activityLabel: String
)
