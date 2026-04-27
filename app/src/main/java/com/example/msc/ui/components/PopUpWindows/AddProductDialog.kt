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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.msc.domain.model.Products
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCborder

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (Products) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

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
                    text = "Añadir Producto",
                    fontFamily = dosisRegular,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueMSCborder
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDialogTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre del producto",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CustomDialogTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = "Precio",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CustomDialogTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = "Cantidad",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }

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
                            val p = price.toDoubleOrNull() ?: 0.0
                            val q = quantity.toIntOrNull() ?: 0
                            onConfirm(Products(name, p, q))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BlueMSC),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Añadir", color = Color.White, fontFamily = dosisRegular)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialogTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = dosisRegular) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BlueMSCborder,
            unfocusedBorderColor = BlueMSC,
            focusedLabelColor = BlueMSCborder,
            unfocusedLabelColor = BlueMSC
        ),
        singleLine = true
    )
}

@Preview
@Composable
fun AddProductDialogPreview() {
    AddProductDialog(onDismiss = {}, onConfirm = {})
}
