package com.example.msc.ui.screen.registerScreen

/**
 * Estado que representa los datos y el flujo de la pantalla de registro.
 * Incluye validaciones visuales y estados de carga.
 */
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
