package com.example.msc.domain.usecase.purchases

import com.example.msc.domain.repository.PurchasesRepository

class DeletePurchaseUseCase(private val repository: PurchasesRepository) {
    suspend operator fun invoke(purchaseId: String) {
        repository.deletePurchase(purchaseId)
    }
}