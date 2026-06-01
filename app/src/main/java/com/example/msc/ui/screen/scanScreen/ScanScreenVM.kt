package com.example.msc.ui.screen.scanScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.data.ticketTemplates.GenericTemplate
import com.example.msc.domain.usecase.purchases.ScanPurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanScreenVM(
    private val scanPurchaseUseCase: ScanPurchaseUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScanScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val genericTemplate = GenericTemplate()

    // Selecciona una imagen y procesa el texto
    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, products = emptyList(), errorMessage = null, detectedTemplate = null, rawScannedText = null) }
            val result = scanPurchaseUseCase(uri)

            // Si el texto se procesa correctamente se actualiza el estado de la vista
            result.onSuccess { text ->
                processScannedText(text, _uiState.value.selectedPattern)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }


    // Procesa el texto y actualiza el estado de la vista
    private fun processScannedText(text: String, pattern: ParsingPattern) {
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotBlank() }

        // Parse sirve para analizar el texto y devolver una lista de productos
        val parsedProducts = genericTemplate.parseWithPattern(lines, pattern)

        _uiState.update { it.copy(
            isLoading = false,
            products = parsedProducts,
            rawScannedText = text
        ) }
    }

    // Cambia el patron de analisis de texto
    fun onPatternChanged(pattern: ParsingPattern) {
        _uiState.update { it.copy(selectedPattern = pattern) }
        val rawText = _uiState.value.rawScannedText
        if (rawText != null) {
            processScannedText(rawText, pattern)
        }
    }
}
