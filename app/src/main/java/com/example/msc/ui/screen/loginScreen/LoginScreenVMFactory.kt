package com.example.msc.ui.screen.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.repository.AuthRepository

class LoginScreenVMFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginScreenVM(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
