package com.example.msc.data.repository

import com.example.msc.domain.model.Purchases
import com.example.msc.domain.repository.PurchasesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepository(private val db: FirebaseFirestore) : PurchasesRepository {

    override suspend fun addPurchase(purchase: Purchases) {
        try {
            // .add() genera automáticamente el ID en Firestore
            db.collection("Purchases").add(purchase).await()
            println("Compra guardada OK")
        } catch (e: Exception) {
            println("Error al guardar compra: $e")
        }
    }

    override suspend fun getPurchases(): List<String> {
        val shops = mutableListOf<String>()
        val result = db.collection("Purchases").get().await()

        for (document in result) {
            val purchase = document.toObject(Purchases::class.java)
            shops.add(purchase.shop)
        }

        return shops
    }

    override suspend fun getPurchasesDetail(): Flow<List<Purchases>> = callbackFlow {
        val result = db.collection("Purchases")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val purchases = value?.toObjects(Purchases::class.java) ?: emptyList()
                trySend(purchases)
            }
        awaitClose { result.remove() }
    }
}
