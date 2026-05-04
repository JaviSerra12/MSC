package com.example.msc.ui.screen.monthlyHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase

class MonthlyHomeScreenVMFactory(
    private val getPurchasesDetailUseCase: GetPurchasesDetailUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyHomeScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthlyHomeScreenVM(getPurchasesDetailUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
