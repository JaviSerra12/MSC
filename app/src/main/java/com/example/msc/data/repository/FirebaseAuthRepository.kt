package com.example.msc.data.repository

import com.example.msc.domain.model.User
import com.example.msc.domain.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
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

    //Cierra la sesión del usuario.
    override fun logout() {
        firebaseAuth.signOut()
    }

    //Cambia el nombre de usuario
    override suspend fun updateUsername(uid: String, newUsername: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).update("username", newUsername).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Cambia el email del usuario
    override suspend fun updateEmail(newEmail: String): Result<Unit> {
        return try {
            firebaseAuth.currentUser?.updateEmail(newEmail)?.await()
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                firestore.collection("users").document(uid).update("email", newEmail).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Cambia la contraseña del usuario
    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            firebaseAuth.currentUser?.updatePassword(newPassword)?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Comprueba el email y la contraseña actual para reautentificar el usuario
    override suspend fun reauthenticate(password: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            val email = user?.email ?: return Result.failure(Exception("No user logged in"))
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
