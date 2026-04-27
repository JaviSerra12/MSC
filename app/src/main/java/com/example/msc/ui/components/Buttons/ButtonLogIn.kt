package com.example.msc.ui.components.Buttons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCerror

@Composable
fun ButtonLogIn(
    texto: String,
    fontSize: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = false
) {

    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = isEnabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BlueMSC,
            disabledContainerColor = BlueMSCerror
        )
    ) {
        Text(
            text = texto,
            fontFamily = dosisRegular,
            fontSize = fontSize.sp
        )
    }
}

@Preview
@Composable
fun ButtonLogInPreview() {
    ButtonLogIn(
        texto = "Iniciar sesión",
        fontSize = 20,
        onClick = {}
    )

}