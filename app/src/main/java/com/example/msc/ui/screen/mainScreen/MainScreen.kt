package com.example.msc.ui.screen.mainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.msc.data.repository.MLKitScanRepository
import com.example.msc.domain.usecase.purchases.ScanPurchaseUseCase
import com.example.msc.ui.navigation.CustomBottomBar
import com.example.msc.ui.navigation.RouteGeneral
import com.example.msc.ui.screen.homeScreen.HomeScreen
import com.example.msc.ui.screen.monthlyHomeScreen.MonthlyHomeScreen
import com.example.msc.ui.screen.profileScreen.ProfileScreen
import com.example.msc.ui.screen.purchasesDetailScreen.PurchasesDetailScreen
import com.example.msc.ui.screen.scanScreen.ScanScreen
import com.example.msc.ui.screen.scanScreen.ScanScreenVM
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.msc.ui.screen.scanScreen.ScanScreenVMFactory

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    var indexSelected by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(bottomNavController) {
        bottomNavController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            // Se usa startsWith en vez de contains para evitar que "MonthlyHomeScreen" coincida con "Home"
            indexSelected = when {
                route?.startsWith(RouteGeneral.MonthlyHomeScreen.route) == true -> 0
                route?.startsWith(RouteGeneral.ScanScreen.route) == true -> 1
                route?.startsWith("Home") == true -> 2 
                route?.startsWith(RouteGeneral.ProfileScreen.route) == true -> 3
                else -> indexSelected
            }
        }
    }

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                navigationHomeScreen = bottomNavController,
                indexSelected = indexSelected,
                onItemClick = { indexSelected = it }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = RouteGeneral.MonthlyHomeScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(RouteGeneral.MonthlyHomeScreen.route) {
                MonthlyHomeScreen(navController = bottomNavController)
            }
            
            composable(RouteGeneral.ScanScreen.route) {
                // Dependencias necesarias
                val scanRepository = remember { MLKitScanRepository(context) }
                val scanPurchaseUseCase = remember { ScanPurchaseUseCase(scanRepository) }
                val scanViewModel: ScanScreenVM = viewModel(
                    factory = ScanScreenVMFactory(scanPurchaseUseCase)
                )
                ScanScreen(viewModel = scanViewModel)
            }

            composable(
                route = RouteGeneral.HomeScreen.route,
                arguments = listOf(navArgument("month") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val month = backStackEntry.arguments?.getString("month") ?: ""
                HomeScreen(navController = bottomNavController, month = month)
            }
            composable(
                route = RouteGeneral.PurchasesDetailScreen.route,
                arguments = listOf(navArgument("purchaseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val purchaseId = backStackEntry.arguments?.getString("purchaseId") ?: ""
                PurchasesDetailScreen(navController = bottomNavController, purchaseId = purchaseId)
            }
            composable(
                route = RouteGeneral.ProfileScreen.route
            ) {
                ProfileScreen(navController = bottomNavController)
            }
        }
    }
}
