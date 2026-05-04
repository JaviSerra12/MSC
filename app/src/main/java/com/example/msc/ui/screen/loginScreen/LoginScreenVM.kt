package com.example.msc.ui.screen.loginScreen

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//Acciones de la pantalla.
class LoginScreenVM(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState = _uiState.asStateFlow()

    //Comprueba que el email es válido.
    fun onEmailChange(value: String) {
        _uiState.update { currentState ->
            val error = !Patterns.EMAIL_ADDRESS.matcher(value).matches()
            currentState.copy(
                email = value,
                isError = error,
                isLoginEnabled = !error && currentState.password.isNotEmpty() && !currentState.isLoading
            )
        }
    }

    //Comprueba que la contraseña es válida.
    fun onPasswordChange(value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = value,
                isLoginEnabled = !currentState.isError && value.isNotEmpty() && !currentState.isLoading
            )
        }
    }

    //Comprueba que el usuario existe en la base de datos.
    fun onLoginClicked(onSuccess: () -> Unit) {
        val email = _uiState.value.email
        val password = _uiState.value.password

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }

            val result = loginUseCase(email, password)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = exception.message ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
