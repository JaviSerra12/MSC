package com.example.msc.ui.components.PopUpWindows

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//OptIn indica que se está usando una función experimental de Jetpack Compose.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Long) -> Unit
) {

    //Variables para los campos del diálogo.
    var shopName by remember { mutableStateOf("") }
    // datePicker viene de la librería de Jetpack Compose
    // showDatePicker indica si se muestra el diálogo de fecha
    var showDatePicker by remember { mutableStateOf(false) }
    // rememberDatePickerState inicia el estado y selecciona la fecha actual
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    // selectedDate obtiene la fecha seleccionada | ?: indica que si es nulo, se ponga la fecha actual
    val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()

    // Da formato a la fecha | Locale.getDefault() indica el idioma del dispositivo
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate))

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {

                // false para que desaparezca el diálogo al pulsar en el botón
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de fecha (desplegable tipo calendario)
                OutlinedTextField(
                    value = formattedDate,
                    onValueChange = { },
                    label = { Text("Fecha de compra", fontFamily = dosisRegular) },

                    // No deja escribir en el campo
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()

                        // Activa el calendario
                        .clickable { showDatePicker = true },
                    enabled = false, // Evita que aparezca el teclado
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledBorderColor = BlueMSC,
                        disabledLabelColor = BlueMSC,
                        disabledLeadingIconColor = BlueMSC,
                        disabledTrailingIconColor = BlueMSC
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
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
                                onConfirm(shopName, selectedDate)
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
    AddShopDialog(onDismiss = {}, onConfirm = { _, _ -> })
}
