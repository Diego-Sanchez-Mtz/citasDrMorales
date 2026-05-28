package com.example.citasdrmorales.home.apponitments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.Appointment
import com.example.citasdrmorales.core.network.AppointmentService
import com.example.citasdrmorales.core.repositories.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentsViewModel (
    private val service: AppointmentService = AppointmentRepository()
): ViewModel(){
    private val _appointmentState = MutableStateFlow<ResponseService<List<Appointment>>?>(null)
    val appointmentState: StateFlow<ResponseService<List<Appointment>>?> = _appointmentState.asStateFlow()

    fun loadAppointments(limit: Int = 5){
        viewModelScope.launch {
            _appointmentState.value = ResponseService.Loading
            _appointmentState.value = service.getAppontments(limit)
        }
    }

}