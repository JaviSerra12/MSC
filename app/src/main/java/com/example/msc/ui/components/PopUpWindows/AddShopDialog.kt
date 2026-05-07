package com.example.msc.ui.components.PopUpWindows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCborder

@Composable
fun AddShopDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {

    //Variables para los campos del diálogo.
    var shopName by remember { mutableStateOf("") }

    //Dialog hace que el contenido se muestre en pantalla.
    //onDismissRequest se ejecuta cuando se pulsa fuera del diálogo.
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nueva Compra",
                    fontFamily = dosisRegular,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueMSCborder
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDialogTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    label = "Nombre de la tienda",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray, fontFamily = dosisRegular)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (shopName.isNotEmpty()) {
                                onConfirm(shopName)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlueMSC),
                        shape = RoundedCornerShape(8.dp),
                        enabled = shopName.isNotEmpty()
                    ) {
                        Text("Continuar", color = Color.White, fontFamily = dosisRegular)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddShopDialogPreview() {
    AddShopDialog(onDismiss = {}, onConfirm = {})
}
