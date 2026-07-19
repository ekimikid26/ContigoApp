package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.audio.AmbientSoundManager
import com.example.proyectotesismovil.data.local.entity.ReflectionEntity
import com.example.proyectotesismovil.domain.repository.EmotionalActivityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReflectionViewModel(
    private val repository: EmotionalActivityRepository
) : ViewModel() {

    private val _isMusicPlaying = MutableStateFlow(false)
    val isMusicPlaying = _isMusicPlaying.asStateFlow()

    private val _currentTrackName = MutableStateFlow(AmbientSoundManager.getCurrentTrackName())
    val currentTrackName = _currentTrackName.asStateFlow()

    private val _isImmersiveMode = MutableStateFlow(false)
    val isImmersiveMode = _isImmersiveMode.asStateFlow()

    val latestReflections: StateFlow<List<ReflectionEntity>> = repository.getLatestReflections()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveReflection(content: String) {
        viewModelScope.launch {
            repository.saveReflection(
                ReflectionEntity(
                    text = content,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearReflections()
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
