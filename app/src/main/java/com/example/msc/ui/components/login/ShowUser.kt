package com.example.msc.ui.components.login

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.ui.components.Text.TextoSecundario

@Composable
fun ShowUser(username: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextoSecundario(
            texto = username,
            size = 16,
            color = Color.DarkGray
        )
    }
}

@Preview
@Composable
fun ShowUserPreview() {
    ShowUser(username = "Javi")
}
