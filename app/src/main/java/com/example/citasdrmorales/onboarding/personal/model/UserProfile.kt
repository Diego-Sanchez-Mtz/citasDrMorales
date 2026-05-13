package com.example.citasdrmorales.onboarding.personal.model

data class UserProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val phone: String,
    val birthDate: String
)