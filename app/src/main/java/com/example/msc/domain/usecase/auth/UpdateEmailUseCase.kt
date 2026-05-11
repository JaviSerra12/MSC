package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository

class UpdateEmailUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(newEmail: String) = repository.updateEmail(newEmail)
}
