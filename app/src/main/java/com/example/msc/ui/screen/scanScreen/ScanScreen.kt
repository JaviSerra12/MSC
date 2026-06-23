package com.example.msc.ui.screen.scanScreen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.msc.ui.components.Buttons.ActionItem
import com.example.msc.ui.components.Buttons.ActionsDropdown
import com.example.msc.ui.components.PopUpWindows.AddProductDialog
import com.example.msc.ui.components.PopUpWindows.AddShopDialog
import com.example.msc.ui.components.PopUpWindows.DeleteConfirmationDialog
import com.example.msc.ui.components.Text.TextoPrincipal
import com.example.msc.ui.components.Text.TextoSecundario
import com.example.msc.ui.components.scan.DetectedProductsList
import com.example.msc.ui.components.scan.PatternDropdown
import com.example.msc.ui.components.scan.ScanningButtons
import com.example.msc.ui.theme.BlueMSC
import com.example.msc.ui.theme.DarkBlueMSC
import com.example.msc.util.FileUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScanScreen(viewModel: ScanScreenVM) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // URI temporal para la foto de la camara
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para la galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    // Launcher para la camara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { viewModel.onImageSelected(it) }
        }
    }

    // Launcher para el permiso de la camara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = FileUtils.createTempPictureUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Pide el nombre de la tienda
    if (uiState.isAddShopDialogVisible) {
        AddShopDialog(
            onDismiss = { viewModel.onDismissAddShopDialog() },
            onConfirm = { shopName, date ->
                viewModel.onConfirmShop(shopName, date)
            }
        )
    }

    // Edita el contenido de la compra
    if (uiState.isEditDialogVisible) {
        val selectedProduct = uiState.selectedProductIndex?.let { uiState.products.getOrNull(it) }
        AddProductDialog(
            shopName = uiState.tempShopName,
            editableProducts = uiState.products,
            initialEditingIndex = uiState.selectedProductIndex,
            editableName = selectedProduct?.name ?: "",
            editablePrice = selectedProduct?.price?.toString() ?: "",
            editableQuantity = selectedProduct?.quantity?.toString() ?: "",
            onDismiss = { viewModel.onDismissEditDialog() },
            onConfirm = { updatedProducts ->
                viewModel.onConfirmEdit(updatedProducts)
            }
        )
    }

    // Confirma si quieres cancelar la compra
    if (uiState.isCancelConfirmationVisible) {
        DeleteConfirmationDialog(
            title = "Cancelar Escaneo",
            text = "¿Estás seguro de que quieres cancelar? Se perderán los datos escaneados.",
            onDismiss = { viewModel.onDismissCancel() },
            onConfirm = { viewModel.onConfirmCancel() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (!uiState.hasScanned) {
            TextoPrincipal(
                texto = "Total: 0.00€",
                size = 32,
                color = DarkBlueMSC
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                TextoSecundario(
                    texto = dateFormat.format(Date()),
                    size = 12,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 10.dp)
                )
                TextoSecundario(texto = "Nueva Compra", size = 32, color = Color.Black)

                ActionsDropdown(
                    actions = emptyList(),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ScanningButtons(
                onGalleryClick = { galleryLauncher.launch("image/*") },
                onCameraClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        val uri = FileUtils.createTempPictureUri(context)
                        tempImageUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                TextoSecundario(
                    texto = "Sigue estos pasos para registrar tu compra",
                    size = 14,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
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
                    TextoSecundario(
                        modifier = Modifier.weight(1f),
                        texto = "Pasos a seguir",
                        size = 18,
                        color = DarkBlueMSC
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = DarkBlueMSC,
                    thickness = 2.dp
                )

                val steps = listOf(
                    "1. Cargar el ticket o hacerle foto",
                    "2. Añadir el nombre de la tienda y la fecha de compra",
                    "3. Comprobar que todos los productos son correctos",
                    "4. Modificar los productos erroneos pulsando 'Acciones'",
                    "5. Aceptar la compra"
                )

                steps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextoSecundario(
                            texto = step,
                            size = 16,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (uiState.hasScanned) {
            val totalPrice = uiState.products.sumOf { it.price * it.quantity }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateStr = dateFormat.format(Date(uiState.tempPurchaseDate))

            TextoPrincipal(
                texto = "Total: ${
                    String.format(Locale.getDefault(), "%.2f", totalPrice)
                }€",
                size = 32,
                color = DarkBlueMSC
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextoSecundario(
                    texto = dateStr,
                    size = 12,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 10.dp)
                )
                TextoSecundario(
                    texto = uiState.tempShopName,
                    size = 32,
                    color = Color.Black
                )

                ActionsDropdown(
                    actions = listOf(
                        ActionItem(if (uiState.isEditMode) "Dejar de Editar" else "Editar") {
                            viewModel.onEditClicked()
                        },
                        ActionItem("Cambiar Estructura") {
                            viewModel.onShowStructureDropdown()
                        }
                    ),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            if (uiState.isStructureDropdownVisible) {
                Spacer(modifier = Modifier.height(8.dp))
                PatternDropdown(
                    selectedPattern = uiState.selectedPattern,
                    onPatternSelected = { viewModel.onPatternChanged(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de productos detectados
            DetectedProductsList(
                products = uiState.products,
                isEditMode = uiState.isEditMode,
                onProductClicked = { index -> viewModel.onProductClicked(index) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextoSecundario(texto = "Total: ", size = 20, color = Color.Black)
                TextoPrincipal(
                    texto = "${String.format(Locale.getDefault(), "%.2f", totalPrice)}€",
                    size = 24,
                    color = DarkBlueMSC
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.onCancelClicked() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { viewModel.onAcceptPurchase() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aceptar")
                }
            }
        }
    }
}
