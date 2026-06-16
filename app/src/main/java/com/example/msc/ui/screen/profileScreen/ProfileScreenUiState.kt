package com.example.msc.ui.screen.profileScreen

data class ProfileScreenUiState(
    val username: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val familyGroupId: String? = null,
    val familyGroupName: String? = null,
    val familyMembers: List<String> = emptyList()
)
