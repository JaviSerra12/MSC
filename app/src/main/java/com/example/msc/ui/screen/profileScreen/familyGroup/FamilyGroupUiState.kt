package com.example.msc.ui.screen.profileScreen.familyGroup

import com.example.msc.domain.model.User

data class FamilyGroupUiState(
    val name: String = "",
    val adminId: String = "",
    val adminName: String = "",
    val members: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGroupDeleted: Boolean = false,
    val isEditMode: Boolean = false,
    val isDeleteDialogVisible: Boolean = false
)
