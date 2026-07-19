package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.domain.model.User
import com.example.proyectotesismovil.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    val currentUser = repository.observeAuthState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.login(email, password)
            result.onSuccess { user ->
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { e ->
                _uiState.value = AuthUiState.Error(e.message ?: "Error desconocido")
                android.util.Log.e("AuthViewModel", "Login error: ${e.message}", e)
            }
        }
    }

    fun register(user: User, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.register(user, password)
            result.onSuccess {
                _uiState.value = AuthUiState.Success(user)
            }.onFailure { e ->
                _uiState.value = AuthUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.sendPasswordResetEmail(email)
            result.onSuccess {
                _uiState.value = AuthUiState.Idle
            }.onFailure { e ->
                _uiState.value = AuthUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun resetPassword(token: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.resetPassword(token, newPassword)
            result.onSuccess {
                _uiState.value = AuthUiState.Idle
            }.onFailure { e ->
                _uiState.value = AuthUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _logoutEvent.emit(Unit)
            } catch (e: Exception) {
                // Aunque falle la limpieza, navegar al login
                _logoutEvent.emit(Unit)
            }
        }
    }
    
    fun clearError() {
        _uiState.value = AuthUiState.Idle
    }
}
