package com.example.msc.domain.usecase.purchases

import com.example.msc.domain.model.Purchases
import com.example.msc.domain.repository.PurchasesRepository
import kotlinx.coroutines.flow.Flow

class GetPurchaseByIdUseCase(private val repository: PurchasesRepository) {
    operator fun invoke(purchaseId: String): Flow<Purchases?> {
        return repository.getPurchaseById(purchaseId)
    }
}
