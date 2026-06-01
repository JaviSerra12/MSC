package com.example.msc.ui.components.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Cards.CustomItem
import com.example.msc.ui.components.PopUpWindows.EditFieldDialog
import com.example.msc.ui.components.PopUpWindows.UpdatePasswordDialog
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.screen.profileScreen.ProfileScreenUiState
import com.example.msc.ui.theme.BlueMSC

@Composable
fun ProfileContent(
    uiState: ProfileScreenUiState,
    onLogoutClick: () -> Unit,
    onUpdateUsername: (String) -> Unit,
    onUpdateEmail: (String) -> Unit,
    onUpdatePassword: (String, String, String, (Result<Unit>) -> Unit) -> Unit
) {
    val context = LocalContext.current

    var showEditUsername by remember { mutableStateOf(false) }
    var showEditEmail by remember { mutableStateOf(false) }
    var showEditPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(BlueMSC, shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = uiState.username.take(1).uppercase(),
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dosisRegular
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CustomItem(
                    text = uiState.username,
                    description = "Toca aquí para cambiar tu nombre de usuario",
                    onClick = { showEditUsername = true }
                )
            }
            item {
                CustomItem(
                    text = uiState.email,
                    description = "Toca aquí para cambiar tu correo electrónico",
                    onClick = { showEditEmail = true }
                )
            }
            item {
                CustomItem(
                    text = "********",
                    description = "Toca aquí para actualizar tu contraseña",
                    onClick = { showEditPassword = true }
                )
            }
            item {
                CustomItem(
                    text = "Cerrar sesión",
                    description = "Haz clic aquí para salir de tu cuenta",
                    onClick = onLogoutClick
                )
            }
        }
    }

    if (showEditUsername) {
        EditFieldDialog(
            title = "Cambiar nombre",
            initialValue = uiState.username,
            onDismiss = { showEditUsername = false },
            onConfirm = {
                onUpdateUsername(it)
                showEditUsername = false
            }
        )
    }

    if (showEditEmail) {
        EditFieldDialog(
            title = "Cambiar email",
            initialValue = uiState.email,
            onDismiss = { showEditEmail = false },
            onConfirm = {
                onUpdateEmail(it)
                showEditEmail = false
            },
            keyboardType = KeyboardType.Email
        )
    }

    if (showEditPassword) {
        UpdatePasswordDialog(
            onDismiss = { showEditPassword = false },
            onConfirm = { current, new, confirm, onResult ->
                onUpdatePassword(current, new, confirm) { result ->
                    onResult(result)
                    if (result.isSuccess) {
                        Toast.makeText(context, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                        showEditPassword = false
                    } else {
                        Toast.makeText(context, result.exceptionOrNull()?.message ?: "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}
