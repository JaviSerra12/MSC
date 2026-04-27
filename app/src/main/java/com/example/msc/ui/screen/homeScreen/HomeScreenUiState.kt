package com.example.msc.ui.screen.homeScreen

import com.example.msc.domain.model.Purchases

data class HomeScreenUiState(
    val purchaseTitles: List<String> = emptyList(),
    val purchaseDetail: List<Purchases> = emptyList(),
    val isAddProductDialogVisible: Boolean = false
)
