package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.model.Purchases
import com.example.msc.domain.model.Products
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesShopUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeScreenVM(
    private val getPurchasesDetailUseCase: GetPurchasesDetailUseCase,
    private val getPurchasesShopUseCase: GetPurchasesShopUseCase,
    private val addPurchaseUseCase: AddPurchaseUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

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
        _uiState.update { it.copy(isAddProductDialogVisible = false) }

        // viewModelScope.launch { repository.addProductToPurchase(product) }
    }

    //Obtener las compras por tienda.
    fun getPurchasesShop() {
        viewModelScope.launch {
            val titles = getPurchasesShopUseCase()
            _uiState.update { it.copy(purchaseTitles = titles) }
        }
    }

    //Obtener el detalle de las compras filtrado por mes.
    fun getPurchasesDetail(monthFilter: String = "") {
        viewModelScope.launch {
            getPurchasesDetailUseCase().collect { purchases ->
                val filteredPurchases = if (monthFilter.isNotEmpty()) {
                    val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    purchases.filter { 
                        formatter.format(Date(it.createdAt)) == monthFilter 
                    }
                } else {
                    purchases
                }
                _uiState.update { it.copy(purchaseDetail = filteredPurchases) }
            }
        }
    }

    //Añade una compra a la base de datos.
    fun addPurchase(purchase: Purchases) {
        viewModelScope.launch {
            addPurchaseUseCase(purchase)
        }
    }
}
