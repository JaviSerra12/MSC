package com.example.msc.ui.screen.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.auth.LogoutUseCase
import com.example.msc.domain.usecase.auth.ReauthenticateUseCase
import com.example.msc.domain.usecase.auth.UpdateEmailUseCase
import com.example.msc.domain.usecase.auth.UpdatePasswordUseCase
import com.example.msc.domain.usecase.auth.UpdateUsernameUseCase

class ProfileScreenVMFactory(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileScreenVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileScreenVM(
                getCurrentUserUseCase,
                getUsernameUseCase,
                logoutUseCase,
                updateUsernameUseCase,
                updateEmailUseCase,
                updatePasswordUseCase,
                reauthenticateUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
