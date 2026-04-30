package com.example.msc.ui.components.Cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.ui.theme.BlueMSC

@Composable
fun CardMonthlyHome(
    month: String,
    totalSpent: Double,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = BlueMSC,
            contentColor = Color.White,
            disabledContainerColor = BlueMSC,
            disabledContentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            ).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = month)
        }
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text("Presupuesto:")
                CustomPriceCard(
                    textPrice = 0.0,
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text("Gastos:")
                CustomPriceCard(
                    textPrice = totalSpent,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun CardMonthlyHomePreview() {
    CardMonthlyHome(
        month = "Enero",
        totalSpent = 150.50,
        onClick = {}
    )
}
