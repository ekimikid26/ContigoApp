package com.example.proyectotesismovil.domain.repository

import com.example.proyectotesismovil.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(user: User, password: String): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun getUserByUid(uid: String): User?
    fun observeAuthState(): Flow<User?>
    suspend fun logout()
    
    // ARCO & Profile
    suspend fun updateProfile(uid: String, updates: Map<String, Any>): Result<Unit>
    suspend fun deleteUserData(uid: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun toggleDataCollection(uid: String, paused: Boolean): Result<Unit>
    suspend fun updateDataSharingConsent(patientUid: String, enabled: Boolean): Result<Unit>
    fun observeDataSharingConsent(patientUid: String): Flow<Boolean>
    
    // Specialist/Admin specific
    fun getPatientsForSpecialist(specialistUid: String): Flow<List<User>>
    fun getAllUsers(): Flow<List<User>>
    suspend fun updateUserStatus(uid: String, active: Boolean): Result<Unit>
    
    // New Invitation-based Linkage
    suspend fun createInvitation(specialistUid: String, specialistNombre: String, patientEmail: String): Result<String>
    suspend fun acceptInvitation(patientUid: String, patientEmail: String, code: String): Result<Unit>
    fun getInvitationsForSpecialist(specialistUid: String): Flow<List<com.example.proyectotesismovil.domain.model.Invitacion>>
}
