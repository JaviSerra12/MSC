package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository

class ReauthenticateUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(password: String) = repository.reauthenticate(password)
}
