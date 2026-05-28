package com.example.msc.ui.screen.purchasesDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.purchases.GetPurchaseByIdUseCase

class PurchasesDetailScreenVMFactory(
    private val purchaseId: String,
    private val getPurchaseByIdUseCase: GetPurchaseByIdUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchasesDetailScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PurchasesDetailScreenVM(purchaseId, getPurchaseByIdUseCase) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
