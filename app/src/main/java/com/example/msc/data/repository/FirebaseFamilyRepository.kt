package com.example.msc.data.repository

import com.example.msc.domain.model.FamilyGroup
import com.example.msc.domain.model.User
import com.example.msc.domain.repository.FamilyRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseFamilyRepository(private val db: FirebaseFirestore) : FamilyRepository {

    override suspend fun createFamilyGroup(name: String, adminId: String): Result<String> {
        return try {
            val newGroupRef = db.collection("familyGroups").document()
            val newGroup = FamilyGroup(
                id = newGroupRef.id,
                name = name,
                adminId = adminId,
                members = listOf(adminId)
            )
            newGroupRef.set(newGroup).await()
            
            // Actualizar el ID del grupo en el usuario administrador
            db.collection("users").document(adminId).update("familyGroupId", newGroupRef.id).await()
            
            Result.success(newGroupRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun inviteUserByEmail(email: String, familyGroupId: String): Result<Unit> {
        return try {
            val userQuery = db.collection("users").whereEqualTo("email", email).get().await()
            if (userQuery.isEmpty) return Result.failure(Exception("Usuario no encontrado"))
            
            val userDoc = userQuery.documents.first()
            val userId = userDoc.id
            val user = userDoc.toObject(User::class.java)
            
            if (user?.familyGroupId != null) return Result.failure(Exception("El usuario ya pertenece a un grupo"))

            // Añade el usuario al grupo
            db.collection("familyGroups").document(familyGroupId)
                .update("members", FieldValue.arrayUnion(userId)).await()
            
            // Actualizar el ID del grupo en el usuario invitado
            db.collection("users").document(userId).update("familyGroupId", familyGroupId).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtiene el grupo de la familia
    override fun getFamilyGroupFlow(familyGroupId: String): Flow<FamilyGroup?> = callbackFlow {
        val listener = db.collection("familyGroups").document(familyGroupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                // snapshot contiene el documento actualizado
                trySend(snapshot?.toObject(FamilyGroup::class.java))
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getFamilyGroup(familyGroupId: String): FamilyGroup? {
        return try {
            db.collection("familyGroups").document(familyGroupId).get().await().toObject(FamilyGroup::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUserById(uid: String): User? {
        return try {
            db.collection("users").document(uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            val query = db.collection("users").whereEqualTo("email", email).get().await()
            query.documents.firstOrNull()?.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateGroupName(familyGroupId: String, newName: String): Result<Unit> {
        return try {
            db.collection("familyGroups").document(familyGroupId).update("name", newName).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeMember(familyGroupId: String, userId: String): Result<Unit> {
        return try {
            // Eliminar el usuario del grupo
            db.collection("familyGroups").document(familyGroupId)
                .update("members", FieldValue.arrayRemove(userId)).await()
            
            // Quitar el ID del grupo en el usuario
            db.collection("users").document(userId).update("familyGroupId", null).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteFamilyGroup(familyGroupId: String): Result<Unit> {
        return try {
            val group = getFamilyGroup(familyGroupId) ?: return Result.failure(Exception("Grupo no encontrado"))
            
            // Quitar todos los usuarios del grupo
            group.members.forEach { uid ->
                db.collection("users").document(uid).update("familyGroupId", null).await()
            }
            
            // Borrar el grupo
            db.collection("familyGroups").document(familyGroupId).delete().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
