package com.example.msc.ui.screen.registerScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msc.data.repository.FirebaseAuthRepository
import com.example.msc.ui.components.Buttons.ButtonLogIn
import com.example.msc.ui.components.login.TextFieldEmail
import com.example.msc.ui.components.login.TextFieldPassword
import com.example.msc.ui.components.login.TextFieldUsername
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.components.Text.TextoSecundario
import com.example.msc.ui.theme.BlueMSC

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {

    //Autentificacion de usuario.
    val authRepository = FirebaseAuthRepository()

    //Logica de la pantalla.
    val viewModel: RegisterScreenVM = viewModel(
        factory = RegisterScreenVMFactory(authRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {

        // Logo / Title Section
        Spacer(modifier = Modifier.height(100.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextoSecundario(
                        texto = "My Safe Cart",
                        size = 16,
                        color = BlueMSC
                    )
                    Spacer(modifier = Modifier.width(80.dp))
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.width(80.dp))
                    TextoPrincipal(
                        texto = "¡Regístrate!",
                        size = 16,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Form Section
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextFieldUsername(
                modifier = Modifier.height(50.dp),
                username = uiState.username,
                onValueChange = { viewModel.onUsernameChange(it) }
            )

            TextFieldEmail(
                modifier = Modifier.height(50.dp),
                email = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                isError = uiState.isEmailError
            )

            TextFieldPassword(
                modifier = Modifier.height(50.dp),
                password = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) }
            )

            TextFieldPassword(
                modifier = Modifier.height(50.dp),
                password = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Action Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonLogIn(
                modifier = Modifier
                    .height(50.dp)
                    .width(280.dp),
                texto = "Registrarse",
                fontSize = 20,
                onClick = {
                    viewModel.onRegisterClicked(onSuccess = { onRegisterSuccess() })
                },
                isEnabled = uiState.isRegisterEnabled && !uiState.isLoading
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen({})
}
