package com.example.citasdrmorales.core.repositories

import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.DoctorModel
import com.example.citasdrmorales.core.network.ApiClient
import com.example.citasdrmorales.core.network.DoctorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoctorRepository: DoctorService {
    private val api = ApiClient.DoctorApi
    override suspend fun getDoctors(limit: Int): ResponseService<List<DoctorModel>> =
        withContext(Dispatchers.IO){
            try{
                val response = api.getDoctors()
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
                    "No se pudo cargar la lista de doctores: ${e.localizedMessage}"
                )
            }
        }
}