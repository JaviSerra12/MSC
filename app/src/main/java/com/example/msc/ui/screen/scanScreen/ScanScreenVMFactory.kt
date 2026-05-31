package com.example.msc.ui.screen.scanScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.purchases.ScanPurchaseUseCase

class ScanScreenVMFactory(
    private val scanPurchaseUseCase: ScanPurchaseUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanScreenVM(scanPurchaseUseCase) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
