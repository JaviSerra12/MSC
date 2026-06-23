package com.example.msc.ui.components.Text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.msc.R

val createRound = FontFamily(Font(R.font.crete_round_regular))
@Composable
fun TextoPrincipal(
    modifier: Modifier = Modifier,
    texto: String,
    size: Int,
    color: Color,
    textAlign: TextAlign? = null,
    fontStyle: FontStyle? = null
) {
    Text(
        text = texto,
        modifier = modifier,
        fontSize = size.sp,
        color = color,
        fontFamily = createRound,
        textAlign = textAlign,
        fontStyle = fontStyle
    )
}

@Preview
@Composable
fun TextoPrincipalPreview() {
    TextoPrincipal(
        texto = "¡Inicia sesión!",
        size = 10,
        color = Color.Black,
    )
}