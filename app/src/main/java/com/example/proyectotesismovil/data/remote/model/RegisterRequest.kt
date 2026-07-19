package com.example.proyectotesismovil.data.remote.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String,
    @SerializedName("rol") val rol: String,
    // Campos paciente
    @SerializedName("edad") val edad: Int? = null,
    @SerializedName("sexo") val sexo: String? = null,
    @SerializedName("emergencia_nombre") 
        val emergenciaNombre: String? = null,
    @SerializedName("emergencia_tel") 
        val emergenciaTel: String? = null,
    @SerializedName("medicamentos") val medicamentos: String? = null,
    @SerializedName("alergias") val alergias: String? = null,
    @SerializedName("plan_tratamiento") 
        val planTratamiento: String? = null,
    // Campos especialista
    @SerializedName("cedula_profesional") 
        val cedulaProfesional: String? = null,
    @SerializedName("institucion_licenciatura") 
        val institucionLicenciatura: String? = null,
    @SerializedName("cedula_especialidad") 
        val cedulaEspecialidad: String? = null,
    @SerializedName("tipo_especialidad") 
        val tipoEspecialidad: String? = null,
    @SerializedName("anios_experiencia") 
        val aniosExperiencia: Int? = null,
    @SerializedName("institucion") val institucion: String? = null,
    @SerializedName("enfoque_terapeutico") 
        val enfoqueTerapeutico: String? = null,
    @SerializedName("historial_medico")
        val historialMedico: String? = null,
    @SerializedName("telefono")
        val telefono: String? = null
)
