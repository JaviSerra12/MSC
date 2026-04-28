package com.example.msc.ui.screen.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
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
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.components.Text.TextoSecundario
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC

@Composable
fun LoginScreen(loginClick: () -> Unit, onRegisterClick: () -> Unit) {

    //Autentificacion de usuario.
    val authRepository = FirebaseAuthRepository()

    //Logica de la pantalla.
    val viewModel: LoginScreenVM = viewModel(
        factory = LoginScreenVMFactory(authRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {

        //Primera seccion - Logo
        Spacer(modifier = Modifier.height(200.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Parte superior
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

            //Parte inferior
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(80.dp))
                    TextoPrincipal(
                        texto = "¡Inicia sesión!",
                        size = 16,
                        color = Color.Black
                    )
                }
            }
        }

        //Segunda seccion - Separacion logo/Inicio
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
        }

        //Tercera seccion - Login
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextFieldEmail(
                modifier = Modifier.height(50.dp),
                email = uiState.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                isError = uiState.isError
            )

            TextFieldPassword(
                modifier = Modifier.height(50.dp),
                password = uiState.password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
            )
        }
        
        Spacer(modifier = Modifier.height(30.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonLogIn(
                modifier = Modifier
                    .height(50.dp)
                    .width(280.dp),
                texto = "Iniciar sesión",
                fontSize = 20,
                onClick = {
                    viewModel.onLoginClicked(onSuccess = { loginClick() })
                },
                isEnabled = uiState.isLoginEnabled && !uiState.isLoading
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "¿No tienes cuenta? Regístrate aquí",
                fontFamily = dosisRegular,
                color = BlueMSC,
                modifier = Modifier
                    .clickable { onRegisterClick() }
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(loginClick = {}, onRegisterClick = {})
}
