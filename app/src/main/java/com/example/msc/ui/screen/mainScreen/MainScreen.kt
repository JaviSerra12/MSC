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

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    var indexSelected by remember { mutableIntStateOf(0) }

    // Sincroniza el índice de la BottomBar con la ruta actual
    // LaunchedEffect hace que el bloque de código se ejecute solo una vez al inicializar el Composable
    LaunchedEffect(bottomNavController) {
        bottomNavController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            indexSelected = when {
                route?.contains(RouteGeneral.MonthlyHomeScreen.route) == true -> 0
                route?.contains("Home") == true -> 1
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
            //Añadir aquí la ruta de Perfil
        }
    }
}
