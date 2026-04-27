package com.example.msc.ui.components.Buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCerror

/**
 * Arquitectura: Componente Stateless (UI pura).
 * No maneja estado interno de lógica de negocio, solo emite el evento click.
 */
@Composable
fun AddPurchaseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White,
            containerColor = BlueMSC,
            disabledContentColor = BlueMSCerror,
            disabledContainerColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "añadir"
        )
    }
}

@Preview
@Composable
fun AddPurchaseButtonPreview() {
    AddPurchaseButton(onClick = {})
}
