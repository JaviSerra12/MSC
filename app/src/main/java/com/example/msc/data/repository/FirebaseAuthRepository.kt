package com.example.msc.data.repository

import com.example.msc.domain.model.User
import com.example.msc.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


//Implementa AuthRepository utilizando Firebase.
//Gestiona la autenticación (Auth) y almacenamiento de perfiles (Firestore).
class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    //Comprueba si el usuario está autenticado.
    override suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Crea un usuario en Auth y si tiene éxito registra su perfil.
    override suspend fun register(email: String, password: String, username: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            //Si no hay usuario, devuelve error.
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    username = username
                )
                //Usa el UID de Auth como ID del documento en Firestore para facilitar búsquedas.
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
            }
            //Devuelve el usuario autenticado.
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Obtiene el perfil del usuario actual.
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // Obtiene el nombre de usuario
    override suspend fun getUsername(uid: String): User? {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    //Cierra la sesión del usuario. (Sin implementar)
    override fun logout() {
        firebaseAuth.signOut()
    }
}
