package com.example.citasdrmorales.home.appointments

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

class AppointmentsViewModel : ViewModel() {

    private val _appointmentState = MutableStateFlow<ResponseService<List<AppointmentFirebase>>?>(null)
    val appointmentState: StateFlow<ResponseService<List<AppointmentFirebase>>?> = _appointmentState.asStateFlow()

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun loadAppointments() {
        viewModelScope.launch {
            _appointmentState.value = ResponseService.Loading

            val userId = auth.currentUser?.uid
            if (userId == null) {
                _appointmentState.value = ResponseService.Error("Usuario no autenticado")
                return@launch
            }

            try {
                // Buscamos en el nodo "citas_pacientes" -> "ID_DEL_USUARIO"
                val snapshot = database.getReference("citas_pacientes")
                    .child(userId)
                    .get()
                    .await() // Esperamos a que Firebase responda usando corrutinas

                val appointmentsList = mutableListOf<AppointmentFirebase>()

                if (snapshot.exists()) {
                    // Si hay citas, recorremos cada una y las convertimos al modelo
                    for (postSnapshot in snapshot.children) {
                        val appointment = postSnapshot.getValue(AppointmentFirebase::class.java)
                        if (appointment != null) {
                            appointmentsList.add(appointment)
                        }
                    }
                }

                // Aquí ocurre la magia: Le pasamos la lista (vacía o llena) al estado Success
                _appointmentState.value = ResponseService.Success(appointmentsList)

            } catch (e: Exception) {
                _appointmentState.value = ResponseService.Error(e.localizedMessage ?: "Error al cargar citas")
            }
        }
    }
}