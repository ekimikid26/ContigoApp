package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.audio.AmbientSoundManager
import com.example.proyectotesismovil.data.local.entity.ActivityLogEntity
import com.example.proyectotesismovil.domain.repository.EmotionalActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeneralActivityViewModel(
    private val repository: EmotionalActivityRepository
) : ViewModel() {

    private val _isMusicPlaying = MutableStateFlow(false)
    val isMusicPlaying = _isMusicPlaying.asStateFlow()

    private val _currentTrackName = MutableStateFlow(AmbientSoundManager.getCurrentTrackName())
    val currentTrackName = _currentTrackName.asStateFlow()

    private val _isImmersiveMode = MutableStateFlow(false)
    val isImmersiveMode = _isImmersiveMode.asStateFlow()

    fun logActivity(type: String, durationSeconds: Int) {
        viewModelScope.launch {
            repository.saveActivityLog(
                ActivityLogEntity(
                    activityType = type,
                    completedAt = System.currentTimeMillis(),
                    durationSeconds = durationSeconds
                )
            )
        }
    }

    fun toggleMusic(context: android.content.Context) {
        if (_isMusicPlaying.value) {
            AmbientSoundManager.pause()
            _isMusicPlaying.value = false
        } else {
            if (AmbientSoundManager.isPlaying()) {
                AmbientSoundManager.resume()
            } else {
                AmbientSoundManager.play(context)
            }
            _isMusicPlaying.value = true
        }
    }

    fun nextTrack(context: android.content.Context) {
        AmbientSoundManager.nextTrack(context)
        _currentTrackName.value = AmbientSoundManager.getCurrentTrackName()
        _isMusicPlaying.value = true
    }

    fun toggleImmersiveMode() {
        _isImmersiveMode.value = !_isImmersiveMode.value
    }
}
