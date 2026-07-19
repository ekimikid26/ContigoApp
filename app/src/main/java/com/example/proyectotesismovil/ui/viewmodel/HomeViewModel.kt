package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.local.dao.Gad7Dao
import com.example.proyectotesismovil.data.local.entity.Gad7ResultEntity
import com.example.proyectotesismovil.data.local.TokenManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gad7Dao: Gad7Dao,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val userIdFlow = tokenManager.getUid()

    val lastGad7Result: StateFlow<Gad7ResultEntity?> =
        userIdFlow.flatMapLatest { uid ->
            flow {
                if (uid != null) {
                    emit(gad7Dao.getLatestResult(uid))
                } else {
                    emit(null)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val needsGad7: StateFlow<Boolean> = lastGad7Result
        .map { last ->
            if (last == null) return@map true
            val daysSince = (System.currentTimeMillis() - last.appliedAt) /
                (1000L * 60 * 60 * 24)
            daysSince >= 7
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Aquí se llamarían a los repositorios para recargar datos
            kotlinx.coroutines.delay(1000) 
            _isRefreshing.value = false
        }
    }
}
