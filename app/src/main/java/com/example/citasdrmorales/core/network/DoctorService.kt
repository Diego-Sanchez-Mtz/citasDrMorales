package com.example.citasdrmorales.core.network

import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.DoctorModel

interface DoctorService {
    suspend fun getDoctors(limit: Int = 20): ResponseService<List<DoctorModel>>
}