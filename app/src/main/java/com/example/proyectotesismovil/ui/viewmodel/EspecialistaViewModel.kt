package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.domain.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EspecialistaViewModel(
    private val authRepository: AuthRepository,
    private val vinculacionRepository: VinculacionRepository,
    private val notaRepository: NotaRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _patients = MutableStateFlow<List<com.example.proyectotesismovil.domain.model.User>>(emptyList())
    val patients: StateFlow<List<com.example.proyectotesismovil.domain.model.User>> = _patients.asStateFlow()

    private val _invitations = MutableStateFlow<List<com.example.proyectotesismovil.domain.model.Invitacion>>(emptyList())
    val invitations: StateFlow<List<com.example.proyectotesismovil.domain.model.Invitacion>> = _invitations.asStateFlow()

    private val _currentUser = MutableStateFlow<com.example.proyectotesismovil.domain.model.User?>(null)
    val currentUser: StateFlow<com.example.proyectotesismovil.domain.model.User?> = _currentUser.asStateFlow()

    private val _alerts = MutableStateFlow<List<com.example.proyectotesismovil.data.remote.api.AlertResponse>>(emptyList())
    val alerts: StateFlow<List<com.example.proyectotesismovil.data.remote.api.AlertResponse>> = _alerts.asStateFlow()

    init {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            _currentUser.value = user
            if (user != null) {
                launch {
                    authRepository.getPatientsForSpecialist(user.uid).collect {
                        _patients.value = it
                    }
                }
                launch {
                    authRepository.getInvitationsForSpecialist(user.uid).collect {
                        _invitations.value = it
                    }
                }
                launch {
                    // Fetch alerts for all patients
                    // In a real scenario, this would be a more complex query
                    _patients.value.forEach { patient ->
                        alertRepository.getPatientAlerts(patient.id).collect { newAlerts ->
                            _alerts.value = (_alerts.value + newAlerts).distinctBy { it.id }.sortedByDescending { it.generated_at }
                        }
                    }
                }
            }
        }
    }

    fun generateInvitation(email: String, onGenerated: (String) -> Unit) {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser() ?: return@launch
            val result = authRepository.createInvitation(user.uid, user.nombre, email)
            result.onSuccess { code ->
                onGenerated(code)
            }
        }
    }

    fun getPatientDetails(uid: String, onResult: (com.example.proyectotesismovil.domain.model.User?) -> Unit) {
        viewModelScope.launch {
            onResult(authRepository.getUserByUid(uid))
        }
    }

    fun observePatientConsent(uid: String): Flow<Boolean> {
        return authRepository.observeDataSharingConsent(uid)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun updateProfile(nombre: String, institucion: String) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            authRepository.updateProfile(user.uid, mapOf("nombre" to nombre, "institucion" to institucion))
            _currentUser.value = authRepository.getCurrentUser()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
        }
    }

    fun unlinkPatient(patientUid: String) {
        viewModelScope.launch {
            // Logic to unlink patient
            // This would normally call a repository method that calls deleteVinculacion
        }
    }

    fun saveClinicalNote(patientId: Int, note: String) {
        viewModelScope.launch {
            notaRepository.saveNota(patientId, note)
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Recargar datos
            kotlinx.coroutines.delay(1000)
            _isRefreshing.value = false
        }
    }
}
