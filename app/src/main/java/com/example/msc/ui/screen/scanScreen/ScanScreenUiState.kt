package com.example.msc.ui.screen.scanScreen

import com.example.msc.domain.model.Products

enum class ParsingPattern(val label: String) {
    AUTOMATIC("Automático"),
    QTY_NAME_PRICE("Cant | Nombre | Precio"),
    NAME_QTY_PRICE("Nombre | Cant | Precio"),
    NAME_PRICE_QTY("Nombre | Precio | Cant"),
    NAME_PRICE("Nombre | Precio")
}

data class ScanScreenUiState(
    val isLoading: Boolean = false,
    // Crea una lsita de productos en función del texto leído
    val products: List<Products> = emptyList(),
    val errorMessage: String? = null,

    // Detecta la plantilla del ticket
    val detectedTemplate: String? = null,
    val selectedPattern: ParsingPattern = ParsingPattern.AUTOMATIC,

    // rawScannedText contiene el texto sin procesar
    val rawScannedText: String? = null,

    // Crea la compra despues del escaneo
    val isAddShopDialogVisible: Boolean = false,
    val tempShopName: String = "",
    val tempPurchaseDate: Long = System.currentTimeMillis(),
    val username: String = "",
    
    // Edita y confirma la compra
    val isEditMode: Boolean = false,
    val isEditDialogVisible: Boolean = false,
    val selectedProductIndex: Int? = null,
    val isCancelConfirmationVisible: Boolean = false,
    val isStructureDropdownVisible: Boolean = false,
    val hasScanned: Boolean = false,

    // IA para el escaneo
    val isAiEnabled: Boolean = false,

    // Confirmación de compra
    val isSuccessDialogVisible: Boolean = false
)
