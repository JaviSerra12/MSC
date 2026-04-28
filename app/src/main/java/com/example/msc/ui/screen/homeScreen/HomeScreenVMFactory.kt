package com.example.msc.ui.screen.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.repository.PurchasesRepository
import com.example.msc.ui.screen.loginScreen.LoginScreenVM


//Sirve para crear el ViewModel con parametros (No se puede crear en la vista) y por eso se usa Factory
class HomeScreenVMFactory(
    private val repository: PurchasesRepository
) : ViewModelProvider.Factory {

    //Posible mejora: añadir if para que no se solape con otro ViewModel.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}