package com.example.msc.ui.components.PopUpWindows

import androidx.compose.runtime.Composable
import java.util.Locale

@Composable
fun SuccessPurchaseDialog(
    shopName: String,
    totalPrice: Double,
    onConfirm: () -> Unit
) {
    GenericSuccessDialog(
        message = "Su compra se ha creado correctamente",
        title = shopName,
        subtitle = "Total: ${String.format(Locale.getDefault(), "%.2f", totalPrice)}€",
        onConfirm = onConfirm
    )
}
