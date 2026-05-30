package com.example.msc.ui.screen.purchasesDetailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.msc.data.remote.database.FirebaseDatabaseProvider
import com.example.msc.data.repository.FirebaseNoteRepository
import com.example.msc.domain.usecase.purchases.DeletePurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchaseByIdUseCase
import com.example.msc.domain.usecase.purchases.UpdatePurchaseUseCase
import com.example.msc.ui.components.Buttons.DeleteButton
import com.example.msc.ui.components.Buttons.EditButton
import com.example.msc.ui.components.PopUpWindows.AddProductDialog
import com.example.msc.ui.components.PopUpWindows.DeleteConfirmationDialog
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.components.Text.TextoSecundario
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.DarkBlueMSC
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PurchasesDetailScreen(navController: NavHostController, purchaseId: String) {
    val databaseProvider = FirebaseDatabaseProvider()
    val db = databaseProvider.getDb()
    val repository = FirebaseNoteRepository(db)
    val getPurchaseByIdUseCase = GetPurchaseByIdUseCase(repository)
    val updatePurchaseUseCase = UpdatePurchaseUseCase(repository)
    val deletePurchaseUseCase = DeletePurchaseUseCase(repository)

    val viewModel: PurchasesDetailScreenVM = viewModel(
        factory = PurchasesDetailScreenVMFactory(
            purchaseId = purchaseId,
            getPurchaseByIdUseCase = getPurchaseByIdUseCase,
            updatePurchaseUseCase = updatePurchaseUseCase,
            deletePurchaseUseCase = deletePurchaseUseCase
        )
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.backEvent.collect {
            navController.popBackStack()
        }
    }

    // Dialog para editar los productos de la compra
    if (uiState.isEditDialogVisible && uiState.purchase != null) {
        val selectedProduct = uiState.selectedProductIndex?.let { uiState.purchase!!.products.getOrNull(it) }

        AddProductDialog(
            shopName = uiState.purchase!!.shop,
            editableProducts = uiState.purchase!!.products, // Se pasa la lista completa para evitar saltos
            initialEditingIndex = uiState.selectedProductIndex, // Se pasa el índice para resaltar la edición
            editableName = selectedProduct?.name ?: "", 
            editablePrice = selectedProduct?.price?.toString() ?: "",
            editableQuantity = selectedProduct?.quantity?.toString() ?: "",
            onDismiss = { viewModel.onDismissEditDialog() },
            onConfirm = { updatedProducts ->
                viewModel.onConfirmEdit(updatedProducts)
            }
        )
    }

    // Dialog de confirmación de borrado
    if (uiState.isDeleteDialogVisible) {
        DeleteConfirmationDialog(
            onDismiss = { viewModel.onDismissDeleteDialog() },
            onConfirm = { viewModel.onConfirmDelete() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            TextoSecundario(texto = "Cargando...", size = 20, color = Color.Gray)
        } else {
            val purchase = uiState.purchase
            if (purchase != null) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateStr = dateFormat.format(Date(purchase.createdAt))

                TextoPrincipal(
                    texto = "Total: ${String.format("%.2f", purchase.totalPrice)}€",
                    size = 32,
                    color = DarkBlueMSC
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ShopName | Fecha | Botones Acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextoSecundario(texto = dateStr, size = 12, color = Color.Black, modifier = Modifier.padding(top = 10.dp))
                    TextoSecundario(texto = purchase.shop, size = 32, color = Color.Black)
                    
                    Row {
                        DeleteButton(
                            onClick = { viewModel.onDeleteClicked() },
                            modifier = Modifier.padding(top = 10.dp, end = 4.dp)
                        )
                        EditButton(
                            onClick = { viewModel.onEditClicked() },
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),

                ) {
                    itemsIndexed(purchase.products) { index, product ->
                        val backgroundColor = if (index % 2 == 0) DarkBlueMSC else BlueMSC
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (uiState.isEditMode) backgroundColor.copy(alpha = 0.6f) else backgroundColor)
                                .clickable(enabled = uiState.isEditMode) {
                                    viewModel.onProductClicked(index)
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
            } else {
                TextoSecundario(texto = "Compra no encontrada", size = 20, color = Color.Red)
            }
        }
    }
}

@Preview
@Composable
fun PurchasesDetailScreenPreview() {
    PurchasesDetailScreen(navController = rememberNavController(), purchaseId = "1")
}
