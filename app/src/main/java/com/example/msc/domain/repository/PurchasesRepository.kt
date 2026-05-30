package com.example.msc.domain.repository

import com.example.msc.domain.model.Purchases
import kotlinx.coroutines.flow.Flow

//Interfaz que define las operaciones de acceso a datos con las compras.
interface PurchasesRepository {
    suspend fun addPurchase(purchase: Purchases)
    suspend fun getPurchases(userId: String) : List<String>
    fun getPurchasesDetail(userId: String) : Flow<List<Purchases>>
    fun getPurchaseById(purchaseId: String): Flow<Purchases?>
    suspend fun deletePurchase(purchaseId: String)
    suspend fun updatePurchase(purchase: Purchases)
}