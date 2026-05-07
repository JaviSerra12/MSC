package com.example.msc.domain.usecase.auth

import com.example.msc.domain.model.User
import com.example.msc.domain.repository.AuthRepository

class GetUsernameUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(uid: String): User? {
        return repository.getUsername(uid)
    }
}
