package com.example.msc.ui.components.PopUpWindows

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCborder

@Composable
fun UpdatePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, (Result<Unit>) -> Unit) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var currentPasswordError by remember { mutableStateOf<String?>(null) }
    val passwordsMatch = newPassword == confirmPassword || confirmPassword.isEmpty()
    var isUpdating by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cambiar contraseña",
                    fontFamily = dosisRegular,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueMSCborder
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                CustomDialogTextField(
                    value = currentPassword,
                    onValueChange = { 
                        currentPassword = it
                        currentPasswordError = null
                    },
                    label = "Contraseña actual",
                    keyboardType = KeyboardType.Password,
                    isError = currentPasswordError != null,
                    supportingText = currentPasswordError
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomDialogTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Nueva contraseña",
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomDialogTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirmar nueva contraseña",
                    keyboardType = KeyboardType.Password,
                    isError = !passwordsMatch && confirmPassword.isNotEmpty(),
                    supportingText = if (!passwordsMatch && confirmPassword.isNotEmpty()) "Las contraseñas no coinciden" else null
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = BlueMSC)
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    
                    TextButton(onClick = onDismiss, enabled = !isUpdating) {
                        Text("Cancelar", color = Color.Gray, fontFamily = dosisRegular)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                                // Se podria añadir validación aquí
                                return@Button
                            }
                            
                            if (newPassword != confirmPassword) return@Button

                            isUpdating = true
                            onConfirm(currentPassword, newPassword, confirmPassword) { result ->
                                isUpdating = false
                                if (result.isSuccess) {
                                    onDismiss()
                                } else {
                                    currentPasswordError = "Contraseña incorrecta"
                                }
                            }
                        },
                        enabled = !isUpdating && passwordsMatch && currentPassword.isNotEmpty() && newPassword.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueMSC),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cambiar", color = Color.White, fontFamily = dosisRegular)
                    }
                }
            }
        }
    }
}
