package com.example.msc.ui.components.Buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.DarkBlueMSC

@Composable
fun ScanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueMSC,
            contentColor = DarkBlueMSC
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontFamily = dosisRegular,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
