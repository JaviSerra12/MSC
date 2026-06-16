package com.example.msc.ui.components.Buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.theme.BlueMSC

data class ActionItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun ActionsDropdown(
    actions: List<ActionItem>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopEnd)) {
        Button(
            onClick = { expanded = true },
            colors = ButtonColors(
                containerColor = BlueMSC,
                contentColor = Color.Black,
                disabledContainerColor = BlueMSC.copy(alpha = 0.5f),
                disabledContentColor = Color.Black
            )
        ) {
            TextoPrincipal(
                texto = "Acciones",
                size = 12,
                color = Color.Black
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            actions.forEach { action ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = action.label,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        expanded = false
                        action.onClick()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ActionsDropdownPreview() {
    ActionsDropdown(
        actions = listOf(
            ActionItem("Editar") {},
            ActionItem("Borrar") {}
        )
    )
}
