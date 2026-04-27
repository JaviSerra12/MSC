package com.example.msc.ui.components.login

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.R
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCborder
import com.example.msc.ui.theme.BlueMSCerror


@Composable

fun TextFieldPassword(modifier: Modifier = Modifier, password : String, onValueChange : (String) -> Unit){
    TextField(
        modifier = modifier,
        value = password,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                text = "Contraseña",
                fontFamily = dosisRegular
                )
                      },
        colors = TextFieldDefaults.colors(
            errorContainerColor = BlueMSCerror,
            focusedContainerColor = BlueMSC,
            unfocusedContainerColor = BlueMSC,
            disabledContainerColor = BlueMSC,
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
        visualTransformation = PasswordVisualTransformation(),
        trailingIcon = {
         Icon(
             contentDescription = "Icono contraseña",
             imageVector = ImageVector.vectorResource(R.drawable.icon_password),
             tint = Color.White
         )
        }
    )
}

@Preview
@Composable
fun TextFieldPasswordPreview(){
    TextFieldPassword(password = "", onValueChange = {})
}