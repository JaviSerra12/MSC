package com.example.msc.ui.screen.scanScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.ScanPurchaseUseCase

class ScanScreenVMFactory(
    private val scanPurchaseUseCase: ScanPurchaseUseCase,
    private val addPurchaseUseCase: AddPurchaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanScreenVM(
                scanPurchaseUseCase,
                addPurchaseUseCase,
                getCurrentUserUseCase,
                getUsernameUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
