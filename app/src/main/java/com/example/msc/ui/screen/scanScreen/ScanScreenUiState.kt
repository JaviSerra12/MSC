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
    val rawScannedText: String? = null
)
