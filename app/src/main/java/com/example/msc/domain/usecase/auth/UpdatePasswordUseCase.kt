package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository

class UpdatePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(newPassword: String) = repository.updatePassword(newPassword)
}
