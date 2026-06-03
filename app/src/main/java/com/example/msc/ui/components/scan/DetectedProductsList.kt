package com.example.msc.ui.components.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.msc.domain.model.Products
import com.example.msc.ui.screen.scanScreen.ParsingPattern

@Composable
fun DetectedProductsList(
    products: List<Products>,
    selectedPattern: ParsingPattern,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Productos Detectados",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Revisa todos los productos antes de continuar",
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Producto", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text(
                text = "Cant.",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Precio",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        products.forEach { product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = String.format("%.2f", product.quantity),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${String.format("%.2f", product.price)}€",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
