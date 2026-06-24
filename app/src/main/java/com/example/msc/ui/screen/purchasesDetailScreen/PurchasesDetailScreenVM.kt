package com.example.msc.ui.screen.purchasesDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.model.Products
import com.example.msc.domain.usecase.purchases.DeletePurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchaseByIdUseCase
import com.example.msc.domain.usecase.purchases.UpdatePurchaseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PurchasesDetailScreenVM(
    private val purchaseId: String,
    private val getPurchaseByIdUseCase: GetPurchaseByIdUseCase,
    private val updatePurchaseUseCase: UpdatePurchaseUseCase,
    private val deletePurchaseUseCase: DeletePurchaseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PurchasesDetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    // Cuando se borran todos los productos, te devuelve a la pantalla
    private val _backEvent = MutableSharedFlow<Unit>()
    val backEvent = _backEvent.asSharedFlow()

    init {
        loadPurchaseDetail()
    }

    private fun loadPurchaseDetail() {
        viewModelScope.launch {
            getPurchaseByIdUseCase(purchaseId).collect { purchase ->
                _uiState.update { it.copy(purchase = purchase, isLoading = false) }
            }
        }
    }

    fun onEditClicked() {
        // Al pulsar editar se activa el modo editar y guarda el estado original
        _uiState.update { 
            it.copy(
                isEditMode = true,
                originalProducts = it.purchase?.products
            ) 
        }
    }

    fun onProductClicked(index: Int) {
        // En modo editar, se pulsa en un producto para editarlo
        if (_uiState.value.isEditMode) {
            _uiState.update { 
                it.copy(
                    selectedProductIndex = index,
                    isEditDialogVisible = true
                ) 
            }
        }
    }

    fun onDismissEditDialog() {
        _uiState.update { it.copy(isEditDialogVisible = false, selectedProductIndex = null) }
    }

    fun onDeleteClicked() {
        _uiState.update { it.copy(isDeleteDialogVisible = true) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun onConfirmDelete() {
        viewModelScope.launch {
            deletePurchaseUseCase(purchaseId)
            _uiState.update { it.copy(isDeleteDialogVisible = false) }
            _backEvent.emit(Unit)
        }
    }

    fun onConfirmEdit(newProducts: List<Products>) {
        // Actualizamos la lista en memoria, pero no persistimos aún
        _uiState.update { state ->
            state.copy(
                purchase = state.purchase?.copy(products = newProducts),
                isEditDialogVisible = false,
                selectedProductIndex = null
            )
        }
    }

    fun onSaveEditClicked() {
        val currentPurchase = _uiState.value.purchase ?: return
        viewModelScope.launch {
            updatePurchaseUseCase(currentPurchase)
            _uiState.update { 
                it.copy(
                    isEditMode = false,
                    isSaveSuccessDialogVisible = true 
                ) 
            }
        }
    }

    fun onCancelEditClicked() {
        _uiState.update { state ->
            state.copy(
                purchase = state.purchase?.copy(products = state.originalProducts ?: state.purchase.products),
                isEditMode = false,
                isCancelSuccessDialogVisible = true
            )
        }
    }

    fun onDismissSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccessDialogVisible = false) }
    }

    fun onDismissCancelSuccess() {
        _uiState.update { it.copy(isCancelSuccessDialogVisible = false) }
    }
}
