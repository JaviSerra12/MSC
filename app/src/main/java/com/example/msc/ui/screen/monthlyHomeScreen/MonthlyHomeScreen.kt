package com.example.msc.ui.screen.monthlyHomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.msc.domain.usecase.purchases.GetMonthlyExpensesUseCase
import com.example.msc.domain.usecase.purchases.GetPurchasesDetailUseCase
import com.example.msc.ui.components.Cards.CardMonthlyHome
import com.example.msc.ui.components.login.ShowUser
import com.example.msc.ui.navigation.RouteGeneral

@Composable
fun MonthlyHomeScreen(navController: NavHostController) {

    //Conexion a la base de datos.
    val databaseProvider = FirebaseDatabaseProvider()
    val db = databaseProvider.getDb()
    val repository = FirebaseNoteRepository(db)
    val authRepository = FirebaseAuthRepository()
    val familyRepository = FirebaseFamilyRepository(db)
    
    // Casos de Uso
    val getPurchasesDetailUseCase = GetPurchasesDetailUseCase(repository)
    val getMonthlyExpensesUseCase = GetMonthlyExpensesUseCase()
    val getCurrentUserUseCase = GetCurrentUserUseCase(authRepository)
    val getUsernameUseCase = GetUsernameUseCase(authRepository)
    val getFamilyGroupUseCase = GetFamilyGroupUseCase(familyRepository)

    //Logica de la pantalla.
    val viewModel : MonthlyHomeScreenVM = viewModel(
        factory = MonthlyHomeScreenVMFactory(
            getPurchasesDetailUseCase, 
            getMonthlyExpensesUseCase,
            getCurrentUserUseCase,
            getUsernameUseCase,
            getFamilyGroupUseCase
        )
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ShowUser(username = uiState.username)
        }

        Text(text = "Gastos Mensuales", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp, top = 16.dp))

        if (uiState.isLoading) {
            Text(text = "Cargando...")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.monthlyTotals.toList()) { (month, total) ->
                    CardMonthlyHome(
                        month = month,
                        totalSpent = total,
                        onClick = { 
                            // Navegar al detalle del mes pasando el nombre del mes como parámetro
                            navController.navigate(RouteGeneral.HomeScreen.createRoute(month))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview
fun MonthlyHomeScreenPreview() {
    MonthlyHomeScreen(navController = rememberNavController())
}
