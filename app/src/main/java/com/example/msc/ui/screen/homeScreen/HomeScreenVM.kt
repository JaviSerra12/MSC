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

//Acciones de la pantalla.
class HomeScreenVM(private val repository: PurchasesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    //Inicializa mostrando el detalle de las compras.
    init {
        getPurchasesDetail()
    }

    //Cuando se pulsa el botón de añadir producto se muestra el diálogo.
    fun onAddProductClicked() {
        _uiState.update { it.copy(isAddProductDialogVisible = true) }
    }

    //Cuando se pulsa fuera del diálogo se oculta.
    fun onDismissAddProductDialog() {
        _uiState.update { it.copy(isAddProductDialogVisible = false) }
    }

    //Cuando se pulsa el botón de añadir producto se añade el producto a la compra actual. (hay que cambiarlo)
    fun onConfirmAddProduct(product: Products) {
        // Aquí se puede implementar la lógica para añadir el producto a una compra actual
        // Por ahora solo se cierra el diálogo.
        _uiState.update { it.copy(isAddProductDialogVisible = false) }

        // viewModelScope.launch { repository.addProductToPurchase(product) }
    }

    //Obtener las compras por tienda.
    fun getPurchasesShop() {
        viewModelScope.launch {
            val titles = repository.getPurchases()
            _uiState.update { it.copy(purchaseTitles = titles) }
        }
    }

    //Obtener el detalle de las compras.
    fun getPurchasesDetail() {
        viewModelScope.launch {
            repository.getPurchasesDetail().collect { purchases ->
                _uiState.update { it.copy(purchaseDetail = purchases) }
            }
        }
    }

    //Añade una compra a la base de datos.
    fun addPurchase(purchase: Purchases) {

        //Hay que hacer que se añada a un ID de compra para añadirlo a la base de datos.
        viewModelScope.launch {
            repository.addPurchase(purchase)
        }
    }
}
