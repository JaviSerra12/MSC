package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesShopUseCase

//Sirve para crear el ViewModel con parametros (No se puede crear en la vista) y por eso se usa Factory
class HomeScreenVMFactory(
    private val getPurchasesDetailUseCase: GetPurchasesDetailUseCase,
    private val getPurchasesShopUseCase: GetPurchasesShopUseCase,
    private val addPurchaseUseCase: AddPurchaseUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenVM(
                getPurchasesDetailUseCase,
                getPurchasesShopUseCase,
                addPurchaseUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
