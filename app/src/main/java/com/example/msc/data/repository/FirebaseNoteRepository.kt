package com.example.msc.data.repository

import com.example.msc.domain.model.Purchases
import com.example.msc.domain.repository.PurchasesRepository
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await

//Implementa PurchasesRepository utilizando Firestore.
class FirebaseNoteRepository(private val db: FirebaseFirestore) : PurchasesRepository {

    //Guarda una compra en Firestore.
    override suspend fun addPurchase(purchase: Purchases) {
        try {
            // .add() genera automáticamente el ID en Firestore
            db.collection("Purchases").add(purchase).await()
            println("Compra guardada correctamente")
        } catch (e: Exception) {
            println("Error al guardar compra: $e")
        }
    }

    //Obtiene las tiendas de los usuarios especificos.
    override suspend fun getPurchases(userIds: List<String>): List<String> {
        if (userIds.isEmpty()) return emptyList()
        val shops = mutableListOf<String>()
        val result = db.collection("Purchases")
            .whereIn("userId", userIds)
            .get().await()

        for (document in result) {
            val purchase = document.toObject(Purchases::class.java)
            shops.add(purchase.shop)
        }

        return shops
    }

    //Obtiene los detalles de las compras de los usuarios especificos.
    override fun getPurchasesDetail(userIds: List<String>): Flow<List<Purchases>> = callbackFlow {
        if (userIds.isEmpty()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        val result = db.collection("Purchases")
            .whereIn("userId", userIds)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                // Al convertir a objeto Firestore no rellena automáticamente el ID del documento en el campo 'id' de la data class.
                // Se mapea manualmente para incluir el ID del documento.
                val purchases = value?.documents?.mapNotNull { doc ->
                    doc.toObject(Purchases::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(purchases)
            }
        awaitClose { result.remove() }
    }

    //Obtiene los detalles de una compra por su ID.
    override fun getPurchaseById(purchaseId: String): Flow<Purchases?> = callbackFlow {
        val result = db.collection("Purchases").document(purchaseId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val purchase = value?.toObject(Purchases::class.java)?.copy(id = value.id)
                trySend(purchase)
            }
        awaitClose { result.remove() }
    }
    //Borra una compra por su ID.
    override suspend fun deletePurchase(purchaseId: String) {
        try {
            db.collection("Purchases").document(purchaseId).delete().await()
            println("Compra eliminada correctamente")
        } catch (e: Exception) {
            println("Error al eliminar compra: $e")
        }
    }
    // Actualiza una compra en Firestore.
    override suspend fun updatePurchase(purchase: Purchases) {
        try {
            db.collection("Purchases").document(purchase.id).set(purchase).await()
            println("Compra actualizada correctamente")
        } catch (e: Exception) {
            println("Error al actualizar compra: $e")
        }
    }
}
