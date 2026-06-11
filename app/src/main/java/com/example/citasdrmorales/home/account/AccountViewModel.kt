package com.example.citasdrmorales.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AccountViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _appointmentsCount = MutableStateFlow(0)
    val appointmentsCount: StateFlow<Int> = _appointmentsCount

    fun loadUserData() {
        // 1. Obtener correo de Firebase Auth
        _userEmail.value = auth.currentUser?.email ?: "usuario@correo.com"

        // 2. Contar citas en Realtime Database
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = database.getReference("citas_pacientes")
                    .child(userId)
                    .get()
                    .await()

                if (snapshot.exists()) {
                    _appointmentsCount.value = snapshot.childrenCount.toInt()
                } else {
                    _appointmentsCount.value = 0
                }
            } catch (e: Exception) {
                _appointmentsCount.value = 0
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}