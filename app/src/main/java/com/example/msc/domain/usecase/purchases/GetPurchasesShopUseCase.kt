package com.example.msc.domain.usecase.purchases

import com.example.msc.domain.repository.PurchasesRepository

// Obtiene la lista de nombres de las tiendas donde se han hecho compras.
class GetPurchasesShopUseCase(private val repository: PurchasesRepository) {
    suspend operator fun invoke(): List<String> {
        return repository.getPurchases()
    }
}
