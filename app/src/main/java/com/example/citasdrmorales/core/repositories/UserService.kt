package com.example.citasdrmorales.core.repositories

import com.example.citasdrmorales.core.ResponseService
import com.example.citasdrmorales.onboarding.personal.model.UserProfile

interface UserService {
    suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit>
}