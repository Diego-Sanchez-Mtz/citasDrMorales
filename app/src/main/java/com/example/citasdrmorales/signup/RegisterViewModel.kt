package com.example.citasdrmorales.signup

import androidx.lifecycle.ViewModel
import com.example.citasdrmorales.core.AuthRepository
import com.example.citasdrmorales.core.ResponseService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class RegisterViewModel: ViewModel() {
    private val authRepository = AuthRepository()

    private val _registerState = MutableStateFlow<ResponseService<FirebaseUser>?>(null)
    val registerState: StateFlow<ResponseService<FirebaseUser>?> = _registerState.asStateFlow()
}