package com.example.msc.ui.screen.purchasesDetailScreen

import com.example.msc.domain.model.Products
import com.example.msc.domain.model.Purchases

data class PurchasesDetailScreenUiState(
    val purchase: Purchases? = null,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val isEditDialogVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val selectedProductIndex: Int? = null,
    val originalProducts: List<Products>? = null,
    val isSaveSuccessDialogVisible: Boolean = false,
    val isCancelSuccessDialogVisible: Boolean = false
)
