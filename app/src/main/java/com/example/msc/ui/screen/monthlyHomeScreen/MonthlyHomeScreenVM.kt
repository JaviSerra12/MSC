package com.example.msc.ui.screen.monthlyHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.usecase.purchases.GetMonthlyExpensesUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonthlyHomeScreenVM(
    private val getPurchasesDetailUseCase: GetPurchasesDetailUseCase,
    private val getMonthlyExpensesUseCase: GetMonthlyExpensesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MonthlyHomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMonthlyPurchases()
    }

    fun getMonthlyPurchases() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPurchasesDetailUseCase().collect { purchases ->
                val monthlyData = getMonthlyExpensesUseCase(purchases)
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
