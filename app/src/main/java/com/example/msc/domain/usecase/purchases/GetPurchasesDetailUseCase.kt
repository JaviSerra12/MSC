package com.example.msc.domain.usecase.purchases

import com.example.msc.domain.model.Purchases
import com.example.msc.domain.repository.PurchasesRepository
import kotlinx.coroutines.flow.Flow

//Muestra los detalles de una compra.
class GetPurchasesDetailUseCase(private val repository: PurchasesRepository) {
    operator fun invoke(): Flow<List<Purchases>> {
        return repository.getPurchasesDetail()
    }
}
