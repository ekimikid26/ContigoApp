package com.example.proyectotesismovil.domain.model

data class User(
    val id: Int = 0,
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rol: String = "paciente",
    val edad: Int? = null,
    val sexo: String? = null,
    val fechaNacimiento: Long? = null,
    val telefono: String? = null,
    
    // Patient Clinical Info
    val emergenciaNombre: String? = null,
    val emergenciaTel: String? = null,
    val medicamentos: String? = null,
    val alergias: String? = null,
    val planTratamiento: String? = null,
    val historialMedico: String? = null,

    // Specialist Professional Info
    val cedulaProfesional: String? = null,
    val licenciaturaInstitucion: String? = null,
    val cedulaEspecialidad: String? = null,
    val tipoEspecialidad: String? = null,
    val aniosExperiencia: Int? = null,
    val institucionActual: String? = null,
    val enfoqueTerapeutico: String? = null,

    val fechaRegistro: Long? = null,
    val activo: Boolean = true,
    val especialistaAsignado: String? = null,
    val especialistaNombre: String? = null,
    val isDataCollectionPaused: Boolean = false,
    val subscriptionPlan: String? = null
)

enum class UserRole(val value: String) {
    PACIENTE("paciente"),
    ESPECIALISTA("especialista"),
    ADMINISTRADOR("administrador")
}
