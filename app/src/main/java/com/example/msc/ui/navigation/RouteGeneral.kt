package com.example.msc.ui.navigation

sealed class RouteGeneral(val route : String) {
    object LoginScreen : RouteGeneral("Login")
    object RegisterScreen : RouteGeneral("Register")
    object MainScreen : RouteGeneral("Main")
    object HomeScreen : RouteGeneral("Home")
    object MonthlyHomeScreen : RouteGeneral("MonthlyHome")
}
