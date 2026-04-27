package com.example.msc.ui.screen.registerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.repository.AuthRepository

class RegisterScreenVMFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterScreenVM(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
