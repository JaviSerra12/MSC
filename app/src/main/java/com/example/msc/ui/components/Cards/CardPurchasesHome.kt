package com.example.msc.ui.components.Cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.msc.domain.model.Purchases
import com.example.msc.ui.theme.BlueMSC

@Composable

fun CardPurchasesHome(
    purchases: Purchases,
    onClick: () -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(-8.dp)) {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(.8f),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardColors(
                containerColor = BlueMSC,
                contentColor = Color.White,
                disabledContainerColor = BlueMSC,
                disabledContentColor = Color.White
            ),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                horizontalAlignment = Alignment.Start,
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomShopCard(
                        text = purchases.shop,
                        color = Color.White
                    )

                    CustomPriceCard(
                        textPrice = purchases.totalPrice(),
                        color = Color.White
                    )

                }

            }

        }

        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(.8f),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.White,
                disabledContainerColor = BlueMSC,
                disabledContentColor = Color.White
            ),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ), // Añadido padding end para el precio
                horizontalAlignment = Alignment.Start,
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CustomUserCard(
                        text = purchases.user,
                        color = Color.Black
                    )

                    CustomDateCard(
                        createdAt = purchases.createdAt,
                        color = Color.Black
                    )

                }

            }

        }
    }

}


@Preview
@Composable
fun CardPurchasesHomePreview() {
    CardPurchasesHome(
        purchases = Purchases(
            shop = "Mercadona",
            products = listOf(),
            createdAt = System.currentTimeMillis(),
        ),
        onClick = {}
    )
}
