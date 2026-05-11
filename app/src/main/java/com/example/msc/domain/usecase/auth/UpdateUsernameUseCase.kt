package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository

class UpdateUsernameUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(uid: String, newUsername: String) = repository.updateUsername(uid, newUsername)
}
