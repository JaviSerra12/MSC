package com.example.msc.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.msc.ui.screen.homeScreen.HomeScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController){
    composable(RouteGeneral.HomeScreen.route){
        HomeScreen(navController = navController)
    }
}
