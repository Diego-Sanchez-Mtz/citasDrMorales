package com.example.citasdrmorales.core.model

data class AppointmentFirebase (
    val idCita: String = "",
    val fechaHora: String = "",
    val doctor: String = "",
    val especialidad: String = "",
    val estado: String = ""
)