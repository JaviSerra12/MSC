package com.example.msc.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.msc.ui.screen.loginScreen.LoginScreen
import com.example.msc.ui.screen.registerScreen.RegisterScreen

//Rutas de la aplicación para iniciar sesión y registrarse.
fun NavGraphBuilder.authGraph(navController: NavController){

    composable(RouteGeneral.LoginScreen.route){
        LoginScreen(
            loginClick = {
                navController.navigate(RouteGeneral.MainScreen.route) {
                    //popUpTo hace que si pulsa atras no vuelvas al login.
                    popUpTo(RouteGeneral.LoginScreen.route) { inclusive = true }
                }
            },
            onRegisterClick = {
                navController.navigate(RouteGeneral.RegisterScreen.route)
            }
        )
    }

    composable(RouteGeneral.RegisterScreen.route){
        RegisterScreen(
            onRegisterSuccess = {
                navController.navigate(RouteGeneral.MainScreen.route) {
                    //popUpTo hace que si pulsa atras no vuelvas al register.
                    popUpTo(RouteGeneral.LoginScreen.route) { inclusive = true }
                }
            }
        )
    }
}
