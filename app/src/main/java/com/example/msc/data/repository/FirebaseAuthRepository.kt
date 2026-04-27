package com.example.msc.data.repository

import com.example.msc.domain.model.User
import com.example.msc.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Implementación de AuthRepository utilizando los servicios de Firebase.
 * Gestiona tanto la autenticación (Auth) como el almacenamiento de perfiles (Firestore).
 */
class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea un usuario en Auth y, si tiene éxito, registra su perfil en la colección 'users' de Firestore.
     */
    override suspend fun register(email: String, password: String, username: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    username = username
                )
                // Usamos el UID de Auth como ID del documento en Firestore para facilitar búsquedas.
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
            }
            
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
