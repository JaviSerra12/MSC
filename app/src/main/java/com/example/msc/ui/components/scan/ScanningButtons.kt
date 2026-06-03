package com.example.msc.ui.components.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable

// TEMPORAL
fun ScanningButtons(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = onGalleryClick) {
            Text(text = "Cargar Ticket")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onCameraClick) {
            Text(text = "Usar Cámara")
        }
    }
}
