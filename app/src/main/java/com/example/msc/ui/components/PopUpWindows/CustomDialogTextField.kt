package com.example.msc.ui.components.PopUpWindows

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.dosisRegular
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.BlueMSCborder

@Composable
fun CustomDialogTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = dosisRegular) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        isError = isError,
        supportingText = supportingText?.let {
            { Text(text = it, fontFamily = dosisRegular, fontSize = 12.sp) }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BlueMSCborder,
            unfocusedBorderColor = BlueMSC,
            focusedLabelColor = BlueMSCborder,
            unfocusedLabelColor = BlueMSC,
            errorBorderColor = Color.Red,
            errorLabelColor = Color.Red,
            errorSupportingTextColor = Color.Red
        ),
        singleLine = true
    )
}