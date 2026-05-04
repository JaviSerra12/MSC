package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

//Registra un usuario con email, contraseña y nombre de usuario.
class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, username: String): Result<FirebaseUser?> {
        return repository.register(email, password, username)
    }
}
