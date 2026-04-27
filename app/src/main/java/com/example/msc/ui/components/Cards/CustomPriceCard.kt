package com.example.msc.ui.components.Cards

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.dosisRegular

@Composable
fun CustomPriceCard(textPrice: Double, modifier: Modifier = Modifier, color: Color) {

    Text(
        text = String.format("%.2f€", textPrice),
        color = color,
        fontFamily = dosisRegular,
        fontSize = 16.sp
    )
}

@Preview
@Composable
fun CustomPriceCardPreview() {
    CustomPriceCard(
        textPrice = 10.0,
        color = Color.White
    )
}



