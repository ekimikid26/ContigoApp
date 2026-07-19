package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectotesismovil.data.local.dao.ActivityLogDao
import com.example.proyectotesismovil.data.local.dao.EmotionalStateDao

class HistoryViewModelFactory(
    private val emotionalStateDao: EmotionalStateDao,
    private val activityLogDao: ActivityLogDao,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(
                emotionalStateDao,
                activityLogDao,
                userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
