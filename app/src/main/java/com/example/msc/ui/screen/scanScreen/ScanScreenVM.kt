package com.example.msc.ui.screen.scanScreen

import androidx.lifecycle.ViewModel
import com.example.msc.domain.usecase.purchases.ScanPurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScanScreenVM(
    private val scanPurchaseUseCase: ScanPurchaseUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScanScreenUiState())
    val uiState = _uiState.asStateFlow()
}
