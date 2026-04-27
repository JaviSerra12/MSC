package com.example.msc.ui.components.login

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCborder
import com.example.msc.ui.theme.BlueMSCerror

@Composable
fun TextFieldUsername(
    modifier: Modifier = Modifier,
    username: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = username,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                "Nombre de usuario",
                fontFamily = dosisRegular
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BlueMSC,
            disabledContainerColor = BlueMSC,
            errorContainerColor = BlueMSCerror,
            unfocusedContainerColor = BlueMSC,
            errorIndicatorColor = BlueMSCborder,
            focusedIndicatorColor = BlueMSCborder,
            disabledIndicatorColor = BlueMSCborder,
            unfocusedIndicatorColor = BlueMSCborder,

            focusedPlaceholderColor = Color.White,
            errorPlaceholderColor = Color.White,
            disabledPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        trailingIcon = {
            Icon(
                contentDescription = "Icono usuario",
                imageVector = Icons.Default.Person,
                tint = Color.White
            )
        }
    )
}

@Composable
@Preview
fun TextFieldUsernamePreview() {
    TextFieldUsername(username = "", onValueChange = {})
}
