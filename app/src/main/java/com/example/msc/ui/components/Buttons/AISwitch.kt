package com.example.msc.ui.components.Buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.DarkBlueMSC

@Composable
fun AISwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextoPrincipal(
            texto = "Escanear con IA",
            size = 16,
            color = Color.Black
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BlueMSC,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = DarkBlueMSC,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AISwitchPreview() {
    AISwitch(
        checked = false,
        onCheckedChange = {}
    )
}
