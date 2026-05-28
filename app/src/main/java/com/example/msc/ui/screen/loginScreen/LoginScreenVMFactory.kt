package com.example.msc.ui.screen.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.auth.LoginUseCase

//Sirve para crear el ViewModel con parametros (No se puede crear en la vista) y por eso se usa Factory
class LoginScreenVMFactory(
    private val loginUseCase: LoginUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginScreenVM(loginUseCase) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
