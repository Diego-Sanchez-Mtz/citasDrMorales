package com.example.citasdrmorales.core.model

import com.google.gson.annotations.SerializedName

data class AppointmentResponse (
    @SerializedName("citas") val results: List<Appointment>
)

data class Appointment(
    @SerializedName("id_cita") val id_cita: String,
    @SerializedName("fecha_hora") val fecha_hora: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("doctor") val doctor: Doctor
)

data class Doctor (
    @SerializedName("nombre_completo") val nombre_completo: String,
    @SerializedName("especialidad") val especialidad: String
)