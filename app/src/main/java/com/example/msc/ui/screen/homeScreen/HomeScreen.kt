package com.example.msc.ui.screen.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.msc.data.repository.FirebaseNoteRepository
import com.example.msc.ui.components.Buttons.AddPurchaseButton
import com.example.msc.ui.components.PopUpWindows.AddProductDialog
import com.example.msc.ui.components.Cards.CardPurchasesHome
import com.example.msc.ui.navigation.CustomBottomBar

@Composable
fun HomeScreen(navController: NavHostController) {

    //Conexion a la base de datos.
    val databaseProvider = FirebaseDatabaseProvider()
    val db = databaseProvider.getDb()
    val repository = FirebaseNoteRepository(db)

    //Logica de la pantalla.
    val viewModel : HomeScreenVM = viewModel(factory = HomeScreenVMFactory(repository))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    //Variables
    var indexSelected by remember { mutableIntStateOf(0) }

    //Mantiene la lista actualizada.
    LaunchedEffect(Unit) {
        viewModel.getPurchasesDetail()
    }

    if (uiState.isAddProductDialogVisible) {
        AddProductDialog(
            onDismiss = { viewModel.onDismissAddProductDialog() },
            onConfirm = { product ->
                viewModel.onConfirmAddProduct(product)
            }
        )
    }

    //Barra de navegación inferior.
    Scaffold(
        bottomBar = {
            CustomBottomBar(
                navigationHomeScreen = navController,
                indexSelected = indexSelected,
                onItemClick = { indexSelected = it }
            )
        }
    ) { paddingValues ->

        //Modificar para hacer bien el Home
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Notas", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.purchaseDetail) { purchase ->
                    CardPurchasesHome(
                        purchases = purchase,
                        onClick = { /* Info Ampliada */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AddPurchaseButton(
                modifier = Modifier.size(60.dp),
                onClick = { viewModel.onAddProductClicked() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
  HomeScreen(navController = rememberNavController())
}
