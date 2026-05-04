package com.example.msc.domain.usecase.purchases

import com.example.msc.domain.model.Purchases
import com.example.msc.domain.repository.PurchasesRepository

//Añade una nueva compra.
class AddPurchaseUseCase(private val repository: PurchasesRepository) {
    suspend operator fun invoke(purchase: Purchases) {
        repository.addPurchase(purchase)
    }
}
