package com.example.msc.ui.components.PopUpWindows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.msc.ui.theme.DarkBlueMSC

@Composable
fun AddProductDialog(
    shopName: String,
    editableProducts: List<Products> = emptyList(),
    initialEditingIndex: Int? = null,
    editableName: String = "",
    editablePrice: String = "",
    editableQuantity: String = "",
    onDismiss: () -> Unit,
    onConfirm: (List<Products>) -> Unit
) {
    var newName by remember { mutableStateOf(editableName) }
    var newPrice by remember { mutableStateOf(editablePrice) }
    var newQuantity by remember { mutableStateOf(editableQuantity) }
    var newProductList by remember { mutableStateOf(editableProducts) }

    // Indice del producto que se está editando.
    var editingIndex by remember { mutableStateOf<Int?>(initialEditingIndex) }

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
                    text = "Compra en $shopName",
                    fontFamily = dosisRegular,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlueMSCborder
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDialogTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = "Nombre del producto",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CustomDialogTextField(
                        value = newPrice,
                        onValueChange = { newPrice = it },
                        label = "Precio",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CustomDialogTextField(
                        value = newQuantity,
                        onValueChange = { newQuantity = it },
                        label = "Cantidad",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (newName.isNotEmpty()) {
                            val p = newPrice.toDoubleOrNull() ?: 0.0
                            val q = newQuantity.toDoubleOrNull() ?: 1.0
                            val product = Products(newName, p, q)
                            
                            if (editingIndex != null) {
                                // Actualiza el producto existente
                                val updatedList = newProductList.toMutableList()
                                updatedList[editingIndex!!] = product
                                newProductList = updatedList
                                editingIndex = null // Reinicia el producto que se está editando
                            } else {
                                // Añadir nuevo producto
                                newProductList = newProductList + product
                            }
                            
                            // Poner los campos a su valor inicial
                            newName = ""
                            newPrice = ""
                            newQuantity = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueMSC),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (editingIndex != null) "Actualizar producto" else "Añadir a la lista",
                        color = Color.White,
                        fontFamily = dosisRegular
                    )
                }

                if (newProductList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Productos añadidos:",
                        fontFamily = dosisRegular,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        itemsIndexed(newProductList) { index, product ->
                            val isEditing = editingIndex == index
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isEditing) BlueMSC.copy(alpha = 0.1f) else Color.Transparent,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .clickable {
                                        // Seleccionar para editar
                                        newName = product.name
                                        newPrice = product.price.toString()
                                        newQuantity = product.quantity.toString()
                                        editingIndex = index
                                    }
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = product.name,
                                    fontFamily = dosisRegular,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = if (isEditing) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "${product.quantity} x ${product.price}€",
                                    fontFamily = dosisRegular,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = {

                                        // RemoveAt elimina el producto de la lista para actualizarlo.
                                        newProductList = newProductList.toMutableList().apply { removeAt(index) }

                                       // Si se borra el producto editable limpia los campos y elimina el producto de la lista.
                                        if (editingIndex == index) {
                                            editingIndex = null
                                            newName = ""
                                            newPrice = ""
                                            newQuantity = ""

                                            // Cuando se borra un producto se actualiza el index de la lista para que el producto 3 sea el 2.
                                        } else if (editingIndex != null && editingIndex!! > index) {
                                            editingIndex = editingIndex!! - 1
                                        }
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Eliminar producto",
                                        tint = DarkBlueMSC,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
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
                            var finalProducts = newProductList.toMutableList()
                            if (newName.isNotEmpty()) {
                                val p = newPrice.toDoubleOrNull() ?: 0.0
                                val q = newQuantity.toDoubleOrNull() ?: 1.0
                                val product = Products(newName, p, q)
                                
                                if (editingIndex != null) {

                                    // Actualiza el producto existente !! hace que el indice no sea nulo.
                                    finalProducts[editingIndex!!] = product
                                } else {
                                    finalProducts.add(product)
                                }
                            }
                            onConfirm(finalProducts) 
                        },
                        enabled = newProductList.isNotEmpty() || newName.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Finalizar", color = Color.White, fontFamily = dosisRegular)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun AddProductDialogPreview() {
    AddProductDialog(onDismiss = {}, onConfirm = {}, shopName = "Mercadona")
}
