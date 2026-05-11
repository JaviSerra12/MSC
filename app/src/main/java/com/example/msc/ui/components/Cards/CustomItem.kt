package com.example.msc.ui.components.Cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.theme.BlueMSC


@Composable
fun CustomItem(
    text: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(color = BlueMSC, shape = RoundedCornerShape(16.dp))
        ) {
            TextoPrincipal(
                modifier = Modifier.padding(16.dp),
                texto = text,
                size = 24,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(40.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp, start = 16.dp),
                text = description,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun CustomItemPreview() {
    CustomItem(
        "Nombre",
        "Haz clic para cambiar tu nombre"
    )
}
