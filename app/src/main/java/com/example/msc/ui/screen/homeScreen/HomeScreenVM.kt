package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.repository.PurchasesRepository
import com.example.msc.domain.model.Purchases
import com.example.msc.domain.model.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenVM(private val repository: PurchasesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPurchasesDetail()
    }

    fun onAddProductClicked() {
        _uiState.update { it.copy(isAddProductDialogVisible = true) }
    }

    fun onDismissAddProductDialog() {
        _uiState.update { it.copy(isAddProductDialogVisible = false) }
    }

    fun onConfirmAddProduct(product: Products) {
        // Aquí podrías implementar la lógica para añadir el producto a una compra actual
        // Por ahora, solo cerramos el diálogo siguiendo el flujo de UI
        _uiState.update { it.copy(isAddProductDialogVisible = false) }
        
        // Ejemplo de Clean Architecture: Llamar a un Use Case o Repository
        // viewModelScope.launch { repository.addProductToPurchase(product) }
    }

    fun getPurchasesShop() {
        viewModelScope.launch {
            val titles = repository.getPurchases()
            _uiState.update { it.copy(purchaseTitles = titles) }
        }
    }

    fun getPurchasesDetail() {
        viewModelScope.launch {
            repository.getPurchasesDetail().collect { purchases ->
                _uiState.update { it.copy(purchaseDetail = purchases) }
            }
        }
    }

    fun addPurchase(purchase: Purchases) {
        viewModelScope.launch {
            repository.addPurchase(purchase)
        }
    }
}
