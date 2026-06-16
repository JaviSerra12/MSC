package com.example.msc.ui.screen.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.msc.data.remote.database.FirebaseDatabaseProvider
import com.example.msc.data.repository.FirebaseAuthRepository
import com.example.msc.data.repository.FirebaseFamilyRepository
import com.example.msc.data.repository.FirebaseNoteRepository
import com.example.msc.domain.usecase.auth.GetCurrentUserUseCase
import com.example.msc.domain.usecase.auth.GetUsernameUseCase
import com.example.msc.domain.usecase.family.GetFamilyGroupUseCase
import com.example.msc.domain.usecase.purchases.AddPurchaseUseCase
import com.example.msc.domain.usecase.purchases.DeletePurchaseUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesShopUseCase
import com.example.msc.ui.components.Buttons.ActionItem
import com.example.msc.ui.components.Buttons.ActionsDropdown
import com.example.msc.ui.components.Buttons.AddPurchaseButton
import com.example.msc.ui.components.PopUpWindows.AddProductDialog
import com.example.msc.ui.components.PopUpWindows.AddShopDialog
import com.example.msc.ui.components.PopUpWindows.DeleteConfirmationDialog
import com.example.msc.ui.components.Cards.CardPurchasesHome
import com.example.msc.ui.components.login.ShowUser
import com.example.msc.ui.navigation.RouteGeneral

@Composable
fun HomeScreen(navController: NavHostController, month: String = "") {

    //Conexion a la base de datos.
    val databaseProvider = FirebaseDatabaseProvider()
    val db = databaseProvider.getDb()
    val repository = FirebaseNoteRepository(db)
    val authRepository = FirebaseAuthRepository()
    val familyRepository = FirebaseFamilyRepository(db)

    // UseCase de la pantalla.
    val getPurchasesDetailUseCase = GetPurchasesDetailUseCase(repository)
    val getPurchasesShopUseCase = GetPurchasesShopUseCase(repository)
    val addPurchaseUseCase = AddPurchaseUseCase(repository)
    val deletePurchaseUseCase = DeletePurchaseUseCase(repository)
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    val getUsernameUseCase = GetUsernameUseCase(authRepository)
    val getFamilyGroupUseCase = GetFamilyGroupUseCase(familyRepository)

    //Logica de la pantalla.
    val viewModel : HomeScreenVM = viewModel(
        factory = HomeScreenVMFactory(
            getPurchasesDetailUseCase,
            getPurchasesShopUseCase,
            addPurchaseUseCase,
            deletePurchaseUseCase,
            getCurrentUserUseCase,
            getUsernameUseCase,
            getFamilyGroupUseCase
        )
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Mantiene los eventos de navegación del ViewModel actualizada
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { purchaseId ->
            navController.navigate(RouteGeneral.PurchasesDetailScreen.createRoute(purchaseId))
        }
    }

    //Mantiene la lista actualizada filtrando por el mes recibido.
    LaunchedEffect(month) {
        viewModel.getPurchasesDetail(month)
    }

    // Diálogo para pedir el nombre de la tienda
    if (uiState.isAddShopDialogVisible) {
        AddShopDialog(
            onDismiss = { viewModel.onDismissAddShopDialog() },
            onConfirm = { shopName, date ->
                viewModel.onConfirmShop(shopName, date)
            }
        )
    }

    // Diálogo para añadir productos a la tienda
    if (uiState.isAddProductDialogVisible) {
        AddProductDialog(
            shopName = uiState.tempShopName,
            onDismiss = { viewModel.onDismissAddProductDialog() },
            onConfirm = { products ->
                viewModel.onConfirmAddProducts(products)
            }
        )
    }

    // Dialog de confirmación para borrar
    if (uiState.isDeleteConfirmationDialogVisible) {
        DeleteConfirmationDialog(
            onDismiss = { viewModel.onDismissDeleteConfirmationDialog() },
            onConfirm = { viewModel.onConfirmDeletePurchase() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShowUser(username = uiState.username)
            ActionsDropdown(
                actions = listOf(
                    ActionItem(label = "Borrar") { viewModel.onDeleteClicked() }
                )
            )
        }

        Text(
            text = if (month.isNotEmpty()) "Compras de $month" else "Todas las Compras", 
            fontSize = 24.sp, 
            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.purchaseDetail) { purchase ->
                CardPurchasesHome(
                    purchases = purchase,
                    isEditMode = uiState.isDeleteMode,
                    onDelete = { viewModel.onDeletePurchase(purchase.id) },
                    onClick = { 
                        viewModel.onPurchaseClicked(purchase.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AddPurchaseButton(
            modifier = Modifier.size(60.dp),
            onClick = { viewModel.onAddPurchaseClicked() }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
  HomeScreen(navController = rememberNavController())
}
