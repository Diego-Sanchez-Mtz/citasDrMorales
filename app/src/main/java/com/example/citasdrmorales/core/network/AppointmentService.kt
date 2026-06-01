package com.example.citasdrmorales.core.network

import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.Appointment

interface AppointmentService {
    suspend fun getAppointments(limit: Int = 5): ResponseService<List<Appointment>>
}