package com.example.citasdrmorales.core.network

import com.example.citasdrmorales.core.model.AppointmentResponse
import retrofit2.Response
import retrofit2.http.GET

interface AppointmentAPI {
    @GET("/")
    suspend fun getAppointments(): Response<AppointmentResponse>
}
