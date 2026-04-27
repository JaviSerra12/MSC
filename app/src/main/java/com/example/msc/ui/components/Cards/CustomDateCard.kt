package com.example.msc.ui.components.Cards


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.dosisRegular
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomDateCard(createdAt: Long, modifier: Modifier = Modifier, color: Color) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = formatter.format(Date(createdAt))

    Text(
        text = date,
        modifier = modifier,
        color = color,
        fontFamily = dosisRegular,
        fontSize = 10.sp
    )
}

@Preview
@Composable
fun CustomDateCardPreview() {
    CustomDateCard(1776874350995, color = Color.White)
}