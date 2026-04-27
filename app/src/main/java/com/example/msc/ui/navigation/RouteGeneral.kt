package com.example.msc.ui.navigation

sealed class RouteGeneral(val route : String) {
    object LoginScreen : RouteGeneral("Login")
    object RegisterScreen : RouteGeneral("Register")
    object HomeScreen : RouteGeneral("Home")
}