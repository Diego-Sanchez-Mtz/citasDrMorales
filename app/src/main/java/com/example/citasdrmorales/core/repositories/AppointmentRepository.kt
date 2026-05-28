package com.example.citasdrmorales.core.repositories

import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.Appointment
import com.example.citasdrmorales.core.network.ApiClient
import com.example.citasdrmorales.core.network.AppointmentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepository: AppointmentService {
    private val api = ApiClient.AppointmentApi
    override suspend fun getAppontments(limit: Int): ResponseService<List<Appointment>> =
        withContext(Dispatchers.IO){
            try{
                val response = api.getAppointments()
                if(response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        ResponseService.Success(body.results)
                    }else{
                        ResponseService.Error("Respuesta vacía del servidor")
                    }
                }else{
                    ResponseService.Error("Error ${response.code()}: ${response.message()}")
                }
            }catch (e: Exception){
                ResponseService.Error(
                    "No se pudieron cargar las citas: ${e.localizedMessage}"
                )
            }
        }
}