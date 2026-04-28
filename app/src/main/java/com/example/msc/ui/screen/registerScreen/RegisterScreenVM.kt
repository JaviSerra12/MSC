package com.example.msc.ui.screen.registerScreen

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//Acciones de la pantalla.
class RegisterScreenVM(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterScreenUiState())
    val uiState = _uiState.asStateFlow()

    //Comprueba que el nombre de usuario es válido.
    fun onUsernameChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = value,
                isRegisterEnabled = validateRegister(currentState.email, currentState.password, currentState.confirmPassword, currentState.isEmailError, value)
            )
        }
    }

    //Comprueba que el email es válido.
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

    //Comprueba que la contraseña es válida.
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

    //Comprueba que la contraseña es válida y sea igual a la anterior.
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

    //Valida que los datos del registro sean válidos.
    private fun validateRegister(email: String, pass: String, confirmPass: String, emailError: Boolean, username: String): Boolean {
        return email.isNotEmpty() && 
               pass.isNotEmpty() && 
               confirmPass.isNotEmpty() && 
               username.isNotEmpty() &&
               pass == confirmPass && 
               !emailError && 
               pass.length >= 6
    }

    //Acepta que el usuario se registre en la base de datos.
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
