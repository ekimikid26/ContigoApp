package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.local.entity.ActivityLogEntity
import com.example.proyectotesismovil.domain.repository.EmotionalActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: EmotionalActivityRepository
) : ViewModel() {

    private val _isImmersiveMode = MutableStateFlow(false)
    val isImmersiveMode = _isImmersiveMode.asStateFlow()

    fun onActivityComplete(durationSeconds: Int) {
        viewModelScope.launch {
            repository.saveActivityLog(
                ActivityLogEntity(
                    activityType = "Música",
                    completedAt = System.currentTimeMillis(),
                    durationSeconds = durationSeconds
                )
            )
        }
    }

    fun toggleImmersiveMode() {
        _isImmersiveMode.value = !_isImmersiveMode.value
    }
}
