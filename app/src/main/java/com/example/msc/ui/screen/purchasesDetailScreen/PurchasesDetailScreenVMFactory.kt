package com.example.msc.ui.screen.purchasesDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.purchases.DeletePurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchaseByIdUseCase
import com.example.msc.domain.usecase.purchases.UpdatePurchaseUseCase

class PurchasesDetailScreenVMFactory(
    private val purchaseId: String,
    private val getPurchaseByIdUseCase: GetPurchaseByIdUseCase,
    private val updatePurchaseUseCase: UpdatePurchaseUseCase,
    private val deletePurchaseUseCase: DeletePurchaseUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PurchasesDetailScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PurchasesDetailScreenVM(
                purchaseId, 
                getPurchaseByIdUseCase, 
                updatePurchaseUseCase,
                deletePurchaseUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
