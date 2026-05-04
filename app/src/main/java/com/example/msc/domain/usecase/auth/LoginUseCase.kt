package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

//Inicia sesión utilizando email y contraseña.
class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser?> {
        return repository.login(email, password)
    }
}
