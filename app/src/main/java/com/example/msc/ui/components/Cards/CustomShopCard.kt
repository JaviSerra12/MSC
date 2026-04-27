package com.example.msc.ui.components.Cards

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.dosisRegular

@Composable

fun CustomShopCard(text: String, modifier: Modifier = Modifier, color: Color) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontFamily = dosisRegular,
        fontSize = 16.sp
    )
}

@Preview
@Composable

fun CustomTitleCardPreview() {
    CustomShopCard(
        text = "Hola",
        color = Color.White
    )

}