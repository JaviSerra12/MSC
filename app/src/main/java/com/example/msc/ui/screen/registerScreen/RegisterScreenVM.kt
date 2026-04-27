package com.example.msc.ui.screen.registerScreen

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la lógica de negocio de la pantalla de registro.
 * Maneja la validación de campos en tiempo real y la comunicación con el repositorio.
 */
class RegisterScreenVM(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterScreenUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Actualiza el nombre de usuario y valida el estado general del formulario.
     */
    fun onUsernameChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = value,
                isRegisterEnabled = validateRegister(currentState.email, currentState.password, currentState.confirmPassword, currentState.isEmailError, value)
            )
        }
    }

    /**
     * Actualiza el email, realiza validación de formato mediante Patterns y actualiza errores.
     */
    fun onEmailChange(value: String) {
        _uiState.update { currentState ->
            val error = value.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(value).matches()
            currentState.copy(
                email = value,
                isEmailError = error,
                isRegisterEnabled = validateRegister(value, currentState.password, currentState.confirmPassword, error, currentState.username)
            )
        }
    }

    /**
     * Gestiona el cambio de contraseña y valida la longitud mínima requerida por Firebase (6 caracteres).
     */
    fun onPasswordChange(value: String) {
        _uiState.update { currentState ->
            val error = value.length < 6 && value.isNotEmpty()
            currentState.copy(
                password = value,
                isPasswordError = error || (currentState.confirmPassword.isNotEmpty() && value != currentState.confirmPassword),
                isRegisterEnabled = validateRegister(currentState.email, value, currentState.confirmPassword, currentState.isEmailError, currentState.username)
            )
        }
    }

    /**
     * Valida que la confirmación coincida exactamente con la contraseña introducida.
     */
    fun onConfirmPasswordChange(value: String) {
        _uiState.update { currentState ->
            val error = value != currentState.password
            currentState.copy(
                confirmPassword = value,
                isPasswordError = error,
                isRegisterEnabled = validateRegister(currentState.email, currentState.password, value, currentState.isEmailError, currentState.username)
            )
        }
    }

    /**
     * Reúne todas las condiciones necesarias para permitir la pulsación del botón de registro.
     */
    private fun validateRegister(email: String, pass: String, confirmPass: String, emailError: Boolean, username: String): Boolean {
        return email.isNotEmpty() && 
               pass.isNotEmpty() && 
               confirmPass.isNotEmpty() && 
               username.isNotEmpty() &&
               pass == confirmPass && 
               !emailError && 
               pass.length >= 6
    }

    /**
     * Ejecuta el proceso de registro en segundo plano y gestiona el éxito o error de la operación.
     */
    fun onRegisterClicked(onSuccess: () -> Unit) {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val username = _uiState.value.username

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, registerError = null) }
            
            val result = authRepository.register(email, password, username)
            
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { exception ->
                _uiState.update { it.copy(
                    isLoading = false, 
                    registerError = exception.message ?: "Error desconocido al registrar"
                ) }
            }
        }
    }
}
