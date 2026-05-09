package com.example.msc.ui.screen.purchasesDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.usecase.purchases.GetPurchaseByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchasesDetailScreenVM(
    private val purchaseId: String,
    private val getPurchaseByIdUseCase: GetPurchaseByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PurchasesDetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPurchaseDetail()
    }

    private fun loadPurchaseDetail() {
        viewModelScope.launch {
            getPurchaseByIdUseCase(purchaseId).collect { purchase ->
                _uiState.update { it.copy(purchase = purchase, isLoading = false) }
            }
        }
    }
}
