package com.example.msc.ui.screen.registerScreen

//Datos del Register.
data class RegisterScreenUiState(
    val username: String = "",
    val email : String = "",
    val password : String = "",
    val confirmPassword: String = "",
    val isEmailError : Boolean = false,
    val isPasswordError : Boolean = false,
    val isRegisterEnabled : Boolean = false,
    val isLoading: Boolean = false,
    val registerError: String? = null
)
