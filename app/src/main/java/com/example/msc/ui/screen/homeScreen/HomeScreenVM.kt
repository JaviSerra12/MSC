package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.model.Purchases
import com.example.msc.domain.model.Products
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.DeletePurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesShopUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val deletePurchaseUseCase: DeletePurchaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getFamilyGroupUseCase: GetFamilyGroupUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    // MutableSharedFlow gestiona los eventos de navegación
    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private var currentUsername: String = ""
    private var currentUserIds: List<String> = emptyList()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val firebaseUser = getCurrentUserUseCase()
        firebaseUser?.let { user ->
            viewModelScope.launch {
                val userData = getUsernameUseCase(user.uid)
                currentUsername = userData?.username ?: user.displayName ?: "User"
                
                val userIds = mutableListOf(user.uid)
                userData?.familyGroupId?.let { groupId ->
                    val familyGroup = getFamilyGroupUseCase(groupId)
                    familyGroup?.members?.let { members ->
                        userIds.addAll(members.filter { it != user.uid })
                    }
                }
                currentUserIds = userIds
                
                _uiState.update { it.copy(username = currentUsername) }

                getPurchasesShop()
                getPurchasesDetail()
            }
        }
    }

    // Muestra todos los productos de una compra
    fun onPurchaseClicked(purchaseId: String) {
        if (_uiState.value.isDeleteMode) return
        viewModelScope.launch {
            _navigationEvent.emit(purchaseId)
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
    fun onConfirmShop(shopName: String, purchaseDate: Long) {
        _uiState.update { 
            it.copy(
                isAddShopDialogVisible = false,
                isAddProductDialogVisible = true,
                tempShopName = shopName,
                tempPurchaseDate = purchaseDate
            ) 
        }
    }

    // Oculta el diálogo de productos
    fun onDismissAddProductDialog() {
        _uiState.update { it.copy(isAddProductDialogVisible = false, tempShopName = "") }
    }

    // Al finalizar la lista de productos, crea la compra y la guarda en la BD
    fun onConfirmAddProducts(products: List<Products>) {
        val firebaseUser = getCurrentUserUseCase()
        val shopName = _uiState.value.tempShopName
        val purchaseDate = _uiState.value.tempPurchaseDate
        val newPurchase = Purchases(
            shop = shopName,
            products = products,
            createdAt = purchaseDate,
            userId = firebaseUser?.uid ?: "",
            user = currentUsername
        )
        
        _uiState.update { it.copy(isAddProductDialogVisible = false, tempShopName = "") }

        viewModelScope.launch {
            addPurchaseUseCase(newPurchase)
        }
    }

    // Obtener las compras por tienda.
    fun getPurchasesShop() {
        if (currentUserIds.isEmpty()) return
        viewModelScope.launch {
            val titles = getPurchasesShopUseCase(currentUserIds)
            _uiState.update { it.copy(purchaseTitles = titles) }
        }
    }

    // Obtener el detalle de las compras filtrado por mes y ordenado por fecha.
    fun getPurchasesDetail(monthFilter: String = "") {
        if (currentUserIds.isEmpty()) return
        viewModelScope.launch {
            getPurchasesDetailUseCase(currentUserIds).collect { purchases ->
                val sortedPurchases = purchases.sortedByDescending { it.createdAt }

                val filteredPurchases = if (monthFilter.isNotEmpty()) {
                    val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    sortedPurchases.filter { 
                        formatter.format(Date(it.createdAt)) == monthFilter 
                    }
                } else {
                    sortedPurchases
                }
                _uiState.update { it.copy(purchaseDetail = filteredPurchases) }
            }
        }
    }

    //Cuando se pulsa editar se cambia el estado de isEditMode a true.

    fun onDeleteClicked() {
        _uiState.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    //Borra una compra
    fun onDeletePurchase(purchaseId: String) {
        _uiState.update { it.copy(isDeleteConfirmationDialogVisible = true, purchaseIdToDelete = purchaseId) }
    }

    // Oculta el diálogo de confirmación
    fun onDismissDeleteConfirmationDialog() {
        _uiState.update { it.copy(isDeleteConfirmationDialogVisible = false, purchaseIdToDelete = null) }
    }

    // Confirma que la compra se borre
    fun onConfirmDeletePurchase() {
        val purchaseId = _uiState.value.purchaseIdToDelete ?: return
        viewModelScope.launch {
            deletePurchaseUseCase(purchaseId)
            _uiState.update { it.copy(isDeleteConfirmationDialogVisible = false, purchaseIdToDelete = null) }
        }
    }
}
