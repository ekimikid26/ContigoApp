package com.example.proyectotesismovil.domain.model

data class Invitacion(
    val id: Int = 0,
    val uid: String = "",
    val codigo: String = "",
    val especialistaUid: String = "",
    val especialistaNombre: String = "",
    val pacienteEmail: String = "",
    val fechaCreacion: Long? = null,
    val estado: String = "pendiente" // "pendiente", "aceptada", "expirada"
)
