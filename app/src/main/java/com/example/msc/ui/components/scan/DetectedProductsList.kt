package com.example.msc.ui.components.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.msc.domain.model.Products
import com.example.msc.ui.components.Text.TextoSecundario
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.DarkBlueMSC

@Composable
fun DetectedProductsList(
    products: List<Products>,
    isEditMode: Boolean = false,
    onProductClicked: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextoSecundario(
            texto = "Productos Detectados",
            size = 24,
            color = DarkBlueMSC,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        TextoSecundario(
            texto = "Revisa todos los productos antes de continuar",
            size = 14,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextoSecundario(modifier = Modifier.weight(1f), texto = "Producto", size = 18, color = DarkBlueMSC)
            TextoSecundario(modifier = Modifier.width(75.dp), texto = "Cant", size = 18, color = DarkBlueMSC)
            TextoSecundario(modifier = Modifier.width(80.dp), texto = "Precio", size = 18, color = DarkBlueMSC)
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), color = DarkBlueMSC, thickness = 2.dp)

        products.forEachIndexed { index, product ->
            val backgroundColor = if (index % 2 == 0) DarkBlueMSC else BlueMSC
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isEditMode) backgroundColor.copy(alpha = 0.6f) else backgroundColor)
                    .clickable(enabled = isEditMode) {
                        onProductClicked(index)
                    }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextoSecundario(
                    modifier = Modifier.weight(1f),
                    texto = product.name,
                    size = 18,
                    color = Color.White
                )
                TextoSecundario(
                    modifier = Modifier.width(60.dp),
                    texto = "x${product.quantity}",
                    size = 18,
                    color = Color.White
                )
                TextoSecundario(
                    modifier = Modifier.width(80.dp),
                    texto = "${String.format("%.2f", product.price)}€",
                    size = 18,
                    color = Color.White
                )
            }
        }
    }
}
