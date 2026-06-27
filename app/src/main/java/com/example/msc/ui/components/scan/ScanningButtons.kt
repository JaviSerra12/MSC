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
import com.example.msc.ui.components.Buttons.AISwitch
import com.example.msc.ui.theme.BlueMSC

@Composable
fun ScanningButtons(
    isAiEnabled: Boolean,
    onAiEnabledChange: (Boolean) -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
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
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueMSC,
                    contentColor = Color.White
                ),
                onClick = onGalleryClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cargar Ticket")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueMSC,
                    contentColor = Color.White
                ),
                onClick = onCameraClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Usar Cámara")
            }
        }
        
        AISwitch(
            checked = isAiEnabled,
            onCheckedChange = onAiEnabledChange
        )
    }
}
