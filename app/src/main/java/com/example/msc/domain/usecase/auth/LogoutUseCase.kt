package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository

//Cierra la sesión del usuario actual.
class LogoutUseCase(private val repository: AuthRepository) {
    operator fun invoke() {
        repository.logout()
    }
}
