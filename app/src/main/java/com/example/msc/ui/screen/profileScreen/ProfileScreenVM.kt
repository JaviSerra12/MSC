package com.example.msc.ui.screen.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.auth.LogoutUseCase
import com.example.msc.domain.usecase.auth.ReauthenticateUseCase
import com.example.msc.domain.usecase.auth.UpdateEmailUseCase
import com.example.msc.domain.usecase.auth.UpdatePasswordUseCase
import com.example.msc.domain.usecase.auth.UpdateUsernameUseCase
import com.example.msc.domain.usecase.family.CreateFamilyGroupUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenVM(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase,
    private val getFamilyGroupUseCase: GetFamilyGroupUseCase,
    private val createFamilyGroupUseCase: CreateFamilyGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        val firebaseUser = getCurrentUserUseCase()
        firebaseUser?.let { user ->
            _uiState.update { it.copy(email = user.email ?: "", isLoading = true) }
            viewModelScope.launch {
                val userData = getUsernameUseCase(user.uid)
                val username = userData?.username ?: user.displayName ?: "User"
                val familyGroupId = userData?.familyGroupId
                
                _uiState.update { 
                    it.copy(
                        username = username,
                        familyGroupId = familyGroupId,
                        isLoading = false
                    ) 
                }

                if (familyGroupId != null) {
                    loadFamilyData(familyGroupId)
                } else {
                    _uiState.update { it.copy(familyGroupName = null, familyMembers = emptyList()) }
                }
            }
        }
    }

    private suspend fun loadFamilyData(familyGroupId: String) {
        val group = getFamilyGroupUseCase(familyGroupId)
        group?.let { familyGroup ->
            val memberNames = familyGroup.members.map { uid ->
                getUsernameUseCase(uid)?.username ?: "Usuario desconocido"
            }
            _uiState.update { 
                it.copy(
                    familyGroupName = familyGroup.name,
                    familyMembers = memberNames
                ) 
            }
        }
    }

    fun onLogoutClicked() {
        logoutUseCase()
        _uiState.update { it.copy(isLoggedOut = true) }
    }

    fun updateUsername(newUsername: String) {
        val uid = getCurrentUserUseCase()?.uid ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = updateUsernameUseCase(uid, newUsername)
            if (result.isSuccess) {
                _uiState.update { it.copy(username = newUsername, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = updateEmailUseCase(newEmail)
            if (result.isSuccess) {
                _uiState.update { it.copy(email = newEmail, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updatePassword(currentPassword: String, newPassword: String, confirmPassword: String, onResult: (Result<Unit>) -> Unit) {
        if (newPassword != confirmPassword) {
            onResult(Result.failure(Exception("Las contraseñas no coinciden")))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val reauthResult = reauthenticateUseCase(currentPassword)
            if (reauthResult.isSuccess) {
                val updateResult = updatePasswordUseCase(newPassword)
                _uiState.update { it.copy(isLoading = false) }
                onResult(updateResult)
            } else {
                _uiState.update { it.copy(isLoading = false) }
                onResult(Result.failure(Exception("La contraseña actual es incorrecta")))
            }
        }
    }

    fun createFamilyGroup(name: String, onResult: (Result<String>) -> Unit) {
        val adminId = getCurrentUserUseCase()?.uid ?: run {
            onResult(Result.failure(Exception("Usuario no autenticado")))
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = createFamilyGroupUseCase(name, adminId)
            if (result.isSuccess) {
                loadUserData()
            }
            _uiState.update { it.copy(isLoading = false) }
            onResult(result)
        }
    }
}
