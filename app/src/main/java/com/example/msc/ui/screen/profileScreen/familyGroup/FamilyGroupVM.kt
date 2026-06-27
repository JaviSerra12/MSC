package com.example.msc.ui.screen.profileScreen.familyGroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FamilyGroupVM(
    private val familyGroupId: String,
    private val getFamilyGroupFlowUseCase: GetFamilyGroupFlowUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val updateGroupNameUseCase: UpdateGroupNameUseCase,
    private val inviteToFamilyUseCase: InviteToFamilyUseCase,
    private val removeFamilyMemberUseCase: RemoveFamilyMemberUseCase,
    private val deleteFamilyGroupUseCase: DeleteFamilyGroupUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FamilyGroupUiState())
    val uiState = _uiState.asStateFlow()

    val currentUserId = getCurrentUserUseCase()?.uid ?: ""

    init {
        observeFamilyGroup()
    }

    private fun observeFamilyGroup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getFamilyGroupFlowUseCase(familyGroupId).collect { group ->
                if (group == null) {
                    _uiState.update { it.copy(isGroupDeleted = true, isLoading = false) }
                    return@collect
                }

                val adminUser = getUsernameUseCase(group.adminId)
                val memberUsers = group.members.mapNotNull { uid ->
                    getUsernameUseCase(uid)
                }

                _uiState.update {
                    it.copy(
                        name = group.name,
                        adminId = group.adminId,
                        adminName = adminUser?.username ?: "Admin",
                        members = memberUsers,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEditClicked() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun onDeleteClicked() {
        _uiState.update { it.copy(isDeleteDialogVisible = true) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun onConfirmDelete() {
        deleteGroup()
        _uiState.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun onLeaveClicked() {
        _uiState.update { it.copy(isLeaveDialogVisible = true) }
    }

    fun onDismissLeaveDialog() {
        _uiState.update { it.copy(isLeaveDialogVisible = false) }
    }

    fun onConfirmLeave() {
        leaveGroup()
        _uiState.update { it.copy(isLeaveDialogVisible = false) }
    }

    fun updateGroupName(newName: String) {
        viewModelScope.launch {
            updateGroupNameUseCase(familyGroupId, newName)
        }
    }

    fun inviteMember(email: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val result = inviteToFamilyUseCase(email, familyGroupId)
            onResult(result)
        }
    }

    fun removeMember(userId: String) {
        viewModelScope.launch {
            removeFamilyMemberUseCase(familyGroupId, userId)
        }
    }

    private fun deleteGroup() {
        viewModelScope.launch {
            val result = deleteFamilyGroupUseCase(familyGroupId)
            if (result.isSuccess) {
                _uiState.update { it.copy(isGroupDeleted = true) }
            }
        }
    }

    private fun leaveGroup() {
        viewModelScope.launch {
            val result = removeFamilyMemberUseCase(familyGroupId, currentUserId)
            if (result.isSuccess) {
                _uiState.update { it.copy(isGroupDeleted = true) }
            }
        }
    }
}
