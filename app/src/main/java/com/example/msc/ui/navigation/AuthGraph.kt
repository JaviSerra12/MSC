package com.example.msc.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.msc.ui.screen.loginScreen.LoginScreen
import com.example.msc.ui.screen.registerScreen.RegisterScreen

fun NavGraphBuilder.authGraph(navController: NavController){

    composable(RouteGeneral.LoginScreen.route){
        LoginScreen(
            loginClick = {
                navController.navigate(RouteGeneral.HomeScreen.route)
            },
            onRegisterClick = {
                navController.navigate(RouteGeneral.RegisterScreen.route)
            }
        )
    }

    composable(RouteGeneral.RegisterScreen.route){
        RegisterScreen(
            onRegisterSuccess = {
                navController.navigate(RouteGeneral.HomeScreen.route)
            }
        )
    }
}