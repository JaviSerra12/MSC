package com.example.msc.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.msc.ui.screen.mainScreen.MainScreen

//Rutas principales de la aplicación.
fun NavGraphBuilder.homeGraph(navController: NavHostController){
    composable(RouteGeneral.MainScreen.route){
        MainScreen(rootNavController = navController)
    }
}
