package com.example.msc.domain.repository

import com.example.msc.domain.model.Purchases
import kotlinx.coroutines.flow.Flow

interface PurchasesRepository {
    suspend fun addPurchase(purchase: Purchases)
    suspend fun getPurchases() : List<String>
    suspend fun getPurchasesDetail() : Flow<List<Purchases>>
}