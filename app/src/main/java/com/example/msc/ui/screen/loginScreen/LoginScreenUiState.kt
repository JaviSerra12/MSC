package com.example.msc.ui.screen.loginScreen

//Datos del login.
data class LoginScreenUiState(
    val email : String = "",
    val password : String = "",
    val isError : Boolean = false,
    val isLoginEnabled : Boolean = false,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val navigation: String = ""
)
