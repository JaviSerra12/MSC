package com.example.msc.ui.components.Buttons

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.GrayMSC

@Composable

fun EditButton(onClick: () -> Unit, modifier: Modifier) {

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonColors(
            containerColor = BlueMSC,
            contentColor = Color.Black,
            disabledContainerColor = BlueMSC.copy(alpha = 0.5f),
            disabledContentColor = Color.Black
        )
    ) {
        TextoPrincipal(
            texto = "Editar",
            size = 12,
            color = Color.Black
        )
    }
}

@Preview
@Composable

fun EditButtonPreview() {
    EditButton(
        onClick = {},
        modifier = Modifier
    )
}
