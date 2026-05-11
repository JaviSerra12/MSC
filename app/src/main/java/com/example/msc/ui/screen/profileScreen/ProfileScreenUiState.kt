package com.example.msc.ui.screen.profileScreen

data class ProfileScreenUiState(
    val username: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)
