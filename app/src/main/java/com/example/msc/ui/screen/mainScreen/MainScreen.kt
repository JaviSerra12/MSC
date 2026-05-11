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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.msc.ui.navigation.CustomBottomBar
import com.example.msc.ui.navigation.RouteGeneral
import com.example.msc.ui.screen.homeScreen.HomeScreen
import com.example.msc.ui.screen.monthlyHomeScreen.MonthlyHomeScreen
import com.example.msc.ui.screen.profileScreen.ProfileScreen
import com.example.msc.ui.screen.purchasesDetailScreen.PurchasesDetailScreen

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    var indexSelected by remember { mutableIntStateOf(0) }

    LaunchedEffect(bottomNavController) {
        bottomNavController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            indexSelected = when {
                route?.contains(RouteGeneral.MonthlyHomeScreen.route) == true -> 0
                route?.contains("Home") == true -> 1
                route?.contains(RouteGeneral.ProfileScreen.route) == true -> 2
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
