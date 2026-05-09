package com.example.msc.domain.repository

import com.example.msc.domain.model.Purchases
import kotlinx.coroutines.flow.Flow

//Interfaz que define las operaciones de acceso a datos con las compras.
interface PurchasesRepository {
    suspend fun addPurchase(purchase: Purchases)
    suspend fun getPurchases() : List<String>
    fun getPurchasesDetail() : Flow<List<Purchases>>
    fun getPurchaseById(purchaseId: String): Flow<Purchases?>
}