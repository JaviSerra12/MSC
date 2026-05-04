package com.example.msc.domain.usecase.auth

import com.example.msc.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

//Obtiene el usuario que tiene la sesión activa actualmente.
class GetCurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke(): FirebaseUser? {
        return repository.getCurrentUser()
    }
}
