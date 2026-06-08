package com.example.citasdrmorales.home.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.core.model.AppointmentFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ScheduleViewModel : ViewModel() {

    // Estado para controlar el resultado del guardado en Firebase
    private val _saveState = MutableStateFlow<ResponseService<String>?>(null)
    val saveState: StateFlow<ResponseService<String>?> = _saveState.asStateFlow()

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val especialidades = listOf("Seleccionar Especialidad", "Cardiología", "Pediatría", "Medicina General")

    private val medicosPorEspecialidad = mapOf(
        "Cardiología" to listOf("Dr. Carlos Mendoza"),
        "Pediatría" to listOf("Dra. Elena Rostova"),
        "Medicina General" to listOf("Dr. Luis Morales")
    )

    fun getDoctorsForSpecialty(specialty: String): List<String> {
        return listOf("Seleccionar Médico") + (medicosPorEspecialidad[specialty] ?: emptyList())
    }

    //Guardamos la cita en Firebase con el UID del usuario actual
    fun saveAppointment(specialty: String, doctor: String, dateTime: String) {
        viewModelScope.launch {
            // Validaciones básicas antes de subir a Firebase
            if (specialty == "Seleccionar Especialidad" || doctor == "Seleccionar Médico") {
                _saveState.value = ResponseService.Error("Por favor, selecciona una especialidad y un médico válidos.")
                return@launch
            }
            if (dateTime.contains("No se ha seleccionado")) {
                _saveState.value = ResponseService.Error("Por favor, selecciona una fecha y hora para tu cita.")
                return@launch
            }

            _saveState.value = ResponseService.Loading

            val userId = auth.currentUser?.uid
            if (userId == null) {
                _saveState.value = ResponseService.Error("Error: Usuario no autenticado.")
                return@launch
            }

            try {
                // Referencia al nodo: citas_pacientes -> UID_del_usuario
                val userAppointmentsRef = database.getReference("citas_pacientes").child(userId)

                // Generamos un ID único en Firebase para esta cita en específico
                val appointmentId = userAppointmentsRef.push().key ?: ""

                val newAppointment = AppointmentFirebase(
                    idCita = appointmentId,
                    fechaHora = dateTime,
                    doctor = doctor,
                    especialidad = specialty,
                    estado = "PENDIENTE"
                )

                // Insertamos el objeto en la base de datos de Firebase
                userAppointmentsRef.child(appointmentId).setValue(newAppointment).await()

                //Notificamos el éxito
                _saveState.value = ResponseService.Success("¡Cita agendada con éxito!")

            } catch (e: Exception) {
                _saveState.value = ResponseService.Error(e.localizedMessage ?: "Error al guardar en la base de datos.")
            }
        }
    }

    /**
     * Limpia el estado para evitar que se repitan los mensajes al volver a entrar a la pantalla.
     */
    fun resetState() {
        _saveState.value = null
    }
}