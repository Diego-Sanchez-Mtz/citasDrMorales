package com.example.citasdrmorales.core.model

import com.google.gson.annotations.SerializedName

data class DoctorModel(
    @SerializedName("nombre_completo") val nombreCompleto: String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("experiencia") val experiencia: String,
    @SerializedName("disponibilidad") val disponibilidad: String
)

data class DoctorResponse(
    @SerializedName("doctores") val results: List<DoctorModel>
)


