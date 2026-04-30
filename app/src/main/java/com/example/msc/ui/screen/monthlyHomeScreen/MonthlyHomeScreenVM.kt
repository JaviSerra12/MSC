package com.example.msc.ui.screen.monthlyHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.model.Purchases
import com.example.msc.domain.repository.PurchasesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonthlyHomeScreenVM(private val repository: PurchasesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MonthlyHomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMonthlyPurchases()
    }

    fun getMonthlyPurchases() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getPurchasesDetail().collect { purchases ->
                val monthlyData = Purchases.totalPriceByMonth(purchases)
                _uiState.update { 
                    it.copy(
                        monthlyTotals = monthlyData,
                        isLoading = false
                    )
                }
            }
        }
    }
}
