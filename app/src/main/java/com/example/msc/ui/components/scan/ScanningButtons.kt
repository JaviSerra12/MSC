package com.example.msc.ui.components.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.msc.ui.theme.BlueMSC

@Composable
fun ScanningButtons(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onAiGalleryClick: () -> Unit,
    onAiCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onAiGalleryClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueMSC,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Cargar IA")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onAiCameraClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueMSC,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Cámara IA")
            }
        }
    }
}
