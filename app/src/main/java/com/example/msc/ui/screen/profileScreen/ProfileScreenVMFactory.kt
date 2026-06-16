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
import com.example.msc.domain.usecase.family.CreateFamilyGroupUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase

class ProfileScreenVMFactory(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase,
    private val getFamilyGroupUseCase: GetFamilyGroupUseCase,
    private val createFamilyGroupUseCase: CreateFamilyGroupUseCase
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
                reauthenticateUseCase,
                getFamilyGroupUseCase,
                createFamilyGroupUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
