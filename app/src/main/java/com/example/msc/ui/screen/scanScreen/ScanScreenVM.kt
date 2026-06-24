package com.example.msc.ui.screen.scanScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.data.ticketTemplates.GenericTemplate
import com.example.msc.domain.model.Products
import com.example.msc.domain.model.Purchases
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.ScanPurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanScreenVM(
    private val scanPurchaseUseCase: ScanPurchaseUseCase,
    private val addPurchaseUseCase: AddPurchaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScanScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val genericTemplate = GenericTemplate()
    private var currentUsername: String = ""

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val firebaseUser = getCurrentUserUseCase()
        firebaseUser?.let { user ->
            viewModelScope.launch {
                val userData = getUsernameUseCase(user.uid)
                currentUsername = userData?.username ?: user.displayName ?: "User"
                _uiState.update { it.copy(username = currentUsername) }
            }
        }
    }

    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, products = emptyList(), errorMessage = null, hasScanned = false) }
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
            rawScannedText = text,
            isAddShopDialogVisible = parsedProducts.isNotEmpty(),
            hasScanned = parsedProducts.isNotEmpty()
        ) }
    }

    // Cambia el patron de analisis de texto
    fun onPatternChanged(pattern: ParsingPattern) {
        _uiState.update { it.copy(selectedPattern = pattern, isStructureDropdownVisible = false) }
        val rawText = _uiState.value.rawScannedText
        if (rawText != null) {
            processScannedText(rawText, pattern)
        }
    }

    fun onDismissAddShopDialog() {
        _uiState.update { it.copy(isAddShopDialogVisible = false) }
    }

    fun onConfirmShop(shopName: String, purchaseDate: Long) {
        _uiState.update { it.copy(
            tempShopName = shopName,
            tempPurchaseDate = purchaseDate,
            isAddShopDialogVisible = false
        ) }
    }

    fun onEditClicked() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun onProductClicked(index: Int) {
        _uiState.update { it.copy(
            selectedProductIndex = index,
            isEditDialogVisible = true
        ) }
    }

    fun onDismissEditDialog() {
        _uiState.update { it.copy(
            isEditDialogVisible = false,
            selectedProductIndex = null
        ) }
    }

    fun onConfirmEdit(updatedProducts: List<Products>) {
        _uiState.update { it.copy(
            products = updatedProducts,
            isEditDialogVisible = false,
            selectedProductIndex = null
        ) }
    }

    fun onShowStructureDropdown() {
        _uiState.update { it.copy(isStructureDropdownVisible = true) }
    }

    fun onAcceptPurchase() {
        val firebaseUser = getCurrentUserUseCase()
        val newPurchase = Purchases(
            shop = _uiState.value.tempShopName,
            products = _uiState.value.products,
            createdAt = _uiState.value.tempPurchaseDate,
            userId = firebaseUser?.uid ?: "",
            user = currentUsername
        )

        viewModelScope.launch {
            addPurchaseUseCase(newPurchase)
            _uiState.update { it.copy(isSuccessDialogVisible = true) }
        }
    }

    fun onDismissSuccessDialog() {
        resetState()
    }

    fun onCancelClicked() {
        _uiState.update { it.copy(isCancelConfirmationVisible = true) }
    }

    fun onConfirmCancel() {
        resetState()
    }

    fun onDismissCancel() {
        _uiState.update { it.copy(isCancelConfirmationVisible = false) }
    }

    private fun resetState() {
        _uiState.update { ScanScreenUiState(username = currentUsername) }
    }
}
