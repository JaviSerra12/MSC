package com.example.msc.ui.screen.purchasesDetailScreen

import com.example.msc.domain.model.Purchases

data class PurchasesDetailScreenUiState(
    val purchase: Purchases? = null,
    val isLoading: Boolean = true
)
