package com.example.msc.ui.screen.homeScreen

import com.example.msc.domain.model.Purchases

//Datos del Home.
data class HomeScreenUiState(
    val purchaseTitles: List<String> = emptyList(),
    val purchaseDetail: List<Purchases> = emptyList(),
    val isAddProductDialogVisible: Boolean = false,
    val isAddShopDialogVisible: Boolean = false,
    val isDeleteConfirmationDialogVisible: Boolean = false,
    val purchaseIdToDelete: String? = null,
    val tempShopName: String = "",
    val tempPurchaseDate: Long = System.currentTimeMillis(),
    val username: String = "",
    val isDeleteMode: Boolean = false,
)
