package com.example.proyectotesismovil.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.domain.model.*
import com.example.proyectotesismovil.domain.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AdminViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val vinculacionRepository: VinculacionRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    val vinculaciones = vinculacionRepository.getAllVinculaciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadUsers()
            kotlinx.coroutines.delay(1000)
            _isRefreshing.value = false
        }
    }

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            userRepository.getAllUsers(null, null)
                .collect { list ->
                    _users.value = list
                }
        }
    }

    fun updateUserStatus(uid: String, active: Boolean) {
        viewModelScope.launch {
            authRepository.updateUserStatus(uid, active)
            loadUsers()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun updateUserFull(
        uid: String,
        nombre: String,
        correo: String,
        rol: String,
        activo: Boolean
    ) {
        viewModelScope.launch {
            val result = userRepository.updateOtherUser(uid, nombre, correo, rol, activo)
            if (result.isSuccess) {
                loadUsers() // Recargar lista después de editar
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            val result = userRepository.deleteUser(userId)
            if (result.isSuccess) {
                loadUsers() // Recargar lista después de eliminar
            }
        }
    }

    fun desvincular(vinculacionId: Int) {
        viewModelScope.launch {
            vinculacionRepository.desvincular(vinculacionId)
        }
    }

    fun vincular(pacienteUid: String, especialistaUid: String) {
        viewModelScope.launch {
            vinculacionRepository.vincular(pacienteUid, especialistaUid)
            loadUsers()
        }
    }
}
