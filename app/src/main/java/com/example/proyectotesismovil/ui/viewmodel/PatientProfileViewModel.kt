package com.example.proyectotesismovil.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.domain.repository.*
import com.example.proyectotesismovil.security.SecurePreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PatientProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val vinculacionRepository: VinculacionRepository,
    private val emotionalRepository: EmotionalActivityRepository,
    private val context: Context
) : ViewModel() {

    val currentUser = authRepository.observeAuthState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activityLogs = emotionalRepository.getAllActivityLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isConsentGiven: StateFlow<Boolean> = currentUser
        .filterNotNull()
        .flatMapLatest { authRepository.observeDataSharingConsent(it.uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun updateProfile(nombre: String, telefono: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            authRepository.updateProfile(user.uid, mapOf("nombre" to nombre, "telefono" to telefono))
        }
    }

    fun updateConsent(consent: Boolean) {
        viewModelScope.launch {
            vinculacionRepository.updateConsent(consent)
        }
    }

    fun linkWithSpecialist(code: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            authRepository.acceptInvitation(user.uid, user.correo, code)
        }
    }

    fun toggleDataCollection(paused: Boolean) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            authRepository.toggleDataCollection(user.uid, paused)
            SecurePreferences.saveBoolean(context, "opposition_right", paused)
        }
    }

    fun deactivateAccount() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            authRepository.updateUserStatus(user.uid, false)
            authRepository.logout()
        }
    }

    fun deleteAccountPermanently() {
        viewModelScope.launch {
            authRepository.deleteAccount()
            authRepository.logout()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Cargar datos
            kotlinx.coroutines.delay(1000)
            _isRefreshing.value = false
        }
    }
}
