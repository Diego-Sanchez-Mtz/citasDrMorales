package com.example.citasdrmorales.core.network

import com.example.citasdrmorales.core.model.DoctorResponse
import retrofit2.Response
import retrofit2.http.GET

interface DoctorAPI {
    @GET("/")
    suspend fun getDoctors(): Response<DoctorResponse>
}
