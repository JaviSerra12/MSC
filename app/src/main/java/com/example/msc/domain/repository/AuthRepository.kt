package com.example.msc.domain.repository

import com.example.msc.domain.model.User
import com.google.firebase.auth.FirebaseUser

//Interfaz que define las operaciones de autenticación permitidas en la aplicación.
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<FirebaseUser?>

    // Registra un nuevo usuario en Firebase Auth y guarda sus datos adicionales en Firestore.
    suspend fun register(email: String, password: String, username: String): Result<FirebaseUser?>

    // Obtiene el usuario actual autenticado en Firebase Auth.
    fun getCurrentUser(): FirebaseUser?

    //Obtiene el nickname (username) del usuario desde Firestore.
    suspend fun getUsername(uid: String): User?

    //Funciones del perfil
    fun logout()

    suspend fun updateUsername(uid: String, newUsername: String): Result<Unit>
    suspend fun updateEmail(newEmail: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun reauthenticate(password: String): Result<Unit>
}
