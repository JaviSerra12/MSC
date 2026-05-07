package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.model.Purchases
import com.example.msc.domain.model.Products
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
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
    private val addPurchaseUseCase: AddPurchaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

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

    // Muestra el diálogo para añadir el nombre de la tienda
    fun onAddPurchaseClicked() {
        _uiState.update { it.copy(isAddShopDialogVisible = true) }
    }

    // Oculta el diálogo de la tienda
    fun onDismissAddShopDialog() {
        _uiState.update { it.copy(isAddShopDialogVisible = false) }
    }

    // Al confirmar la tienda guarda el nombre y muestra el diálogo de productos
    fun onConfirmShop(shopName: String) {
        _uiState.update { 
            it.copy(
                isAddShopDialogVisible = false,
                isAddProductDialogVisible = true,
                tempShopName = shopName
            ) 
        }
    }

    // Oculta el diálogo de productos
    fun onDismissAddProductDialog() {
        _uiState.update { it.copy(isAddProductDialogVisible = false, tempShopName = "") }
    }

    // Al finalizar la lista de productos, crea la compra y la guarda en la BD
    fun onConfirmAddProducts(products: List<Products>) {
        val shopName = _uiState.value.tempShopName
        val newPurchase = Purchases(
            shop = shopName,
            products = products,
            createdAt = System.currentTimeMillis(),
            user = currentUsername
        )
        
        _uiState.update { it.copy(isAddProductDialogVisible = false, tempShopName = "") }

        viewModelScope.launch {
            addPurchaseUseCase(newPurchase)
        }
    }

    // Obtener las compras por tienda.
    fun getPurchasesShop() {
        viewModelScope.launch {
            val titles = getPurchasesShopUseCase()
            _uiState.update { it.copy(purchaseTitles = titles) }
        }
    }

    // Obtener el detalle de las compras filtrado por mes.
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
}
