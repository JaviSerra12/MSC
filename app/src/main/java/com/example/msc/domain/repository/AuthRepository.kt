package com.example.msc.domain.repository

import com.google.firebase.auth.FirebaseUser

//Interfaz que define las operaciones de autenticación permitidas en la aplicación.
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<FirebaseUser?>

    //Registra un nuevo usuario en Firebase Auth y guarda sus datos adicionales en Firestore.
    suspend fun register(email: String, password: String, username: String): Result<FirebaseUser?>

    fun getCurrentUser(): FirebaseUser?

    fun logout()
}
