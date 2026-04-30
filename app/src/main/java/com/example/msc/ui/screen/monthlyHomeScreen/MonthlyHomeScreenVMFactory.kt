package com.example.msc.ui.screen.monthlyHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.repository.PurchasesRepository

class MonthlyHomeScreenVMFactory(
    private val repository: PurchasesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyHomeScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthlyHomeScreenVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
