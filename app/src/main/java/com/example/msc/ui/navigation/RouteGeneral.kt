package com.example.msc.ui.navigation

sealed class RouteGeneral(val route : String) {
    object LoginScreen : RouteGeneral("Login")
    object RegisterScreen : RouteGeneral("Register")
    object MainScreen : RouteGeneral("Main")
    
    // Home acepta un parámetro de mes opcional. 
    // Si no se pasa el parámetro, mostrará todas las compras.
    object HomeScreen : RouteGeneral("Home?month={month}") {
        fun createRoute(month: String) = "Home?month=$month"
    }

    object MonthlyHomeScreen : RouteGeneral("MonthlyHome")

    object PurchasesDetailScreen : RouteGeneral("PurchasesDetail/{purchaseId}") {
        fun createRoute(purchaseId: String) = "PurchasesDetail/$purchaseId"
    }

    object ProfileScreen : RouteGeneral("Profile")
}
