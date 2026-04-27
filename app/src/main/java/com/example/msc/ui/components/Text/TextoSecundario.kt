package com.example.msc.ui.components.Text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.msc.R
import com.example.msc.ui.theme.BlueMSC


val dosisRegular = FontFamily(Font(R.font.dosis_regular))
@Composable
fun TextoSecundario(modifier: Modifier = Modifier, texto: String, size: Int, color: Color){

    Text(
        modifier = modifier,
        text = texto,
        fontSize = size.sp,
        color = color,
        fontFamily = dosisRegular
    )

}

@Preview
@Composable
fun TextoSecundarioPreview(){
    TextoSecundario(
        texto = "My Safe Cart",
        size = 10,
        color = BlueMSC
    )
}