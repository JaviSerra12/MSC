package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.DeletePurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesShopUseCase

//Sirve para crear el ViewModel con parametros (No se puede crear en la vista) y por eso se usa Factory
class HomeScreenVMFactory(
    private val getPurchasesDetailUseCase: GetPurchasesDetailUseCase,
    private val getPurchasesShopUseCase: GetPurchasesShopUseCase,
    private val addPurchaseUseCase: AddPurchaseUseCase,
    private val deletePurchaseUseCase: DeletePurchaseUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getFamilyGroupUseCase: GetFamilyGroupUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenVM(
                getPurchasesDetailUseCase,
                getPurchasesShopUseCase,
                addPurchaseUseCase,
                deletePurchaseUseCase,
                getCurrentUserUseCase,
                getUsernameUseCase,
                getFamilyGroupUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
