package com.example.msc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.msc.ui.navigation.RouteGeneral
import com.example.msc.ui.navigation.authGraph
import com.example.msc.ui.navigation.homeGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navigationGeneral = rememberNavController()

            NavHost(
                navController = navigationGeneral,
                startDestination = RouteGeneral.LoginScreen.route
            ) {

                authGraph(navController = navigationGeneral)
                homeGraph(navController = navigationGeneral)

            }

        }
    }
}
