package com.example.citasdrmorales.home.doctors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.DoctorModel
import com.example.citasdrmorales.core.network.DoctorService
import com.example.citasdrmorales.core.repositories.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorsViewModel (
    private val service: DoctorService = DoctorRepository()
): ViewModel(){
    private val _doctorsState = MutableStateFlow<ResponseService<List<DoctorModel>>?>(null)
    val doctorsState: StateFlow<ResponseService<List<DoctorModel>>?> = _doctorsState.asStateFlow()

    fun loadDoctors(limit: Int = 5){
        viewModelScope.launch {
            _doctorsState.value = ResponseService.Loading
            _doctorsState.value = service.getDoctors(limit)
        }
    }

}