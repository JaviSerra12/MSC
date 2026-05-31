package com.example.msc.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.msc.ui.theme.BlueMSC


//Barra de navegación inferior.
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val navigate: (NavHostController) -> Unit,
    val contentDescription: String
)

val menuItems = listOf(
    MenuItem(
        title = "Home",
        icon = Icons.Default.Home,
        navigate = {
            it.navigate(RouteGeneral.MonthlyHomeScreen.route)
        },
        contentDescription = "Inicio"
    ),
    MenuItem(
        title = "Tickets",
        icon = Icons.Default.Create,
        navigate = {
            it.navigate(RouteGeneral.ScanScreen.route)
        },
        contentDescription = "Tickets"
    ),
    MenuItem(
        title = "Compras",
        icon = Icons.Default.ShoppingCart,
        navigate = {
            it.navigate("Home")
        },
        contentDescription = "Compras"
    ),
    MenuItem(
        title = "Perfil",
        icon = Icons.Default.Person,
        navigate = {
            it.navigate(RouteGeneral.ProfileScreen.route)
        },
        contentDescription = "Perfil"
    ),

)

@Composable
fun CustomBottomBar(
    navigationHomeScreen: NavHostController,
    onItemClick: (index: Int) -> Unit,
    indexSelected: Int
) {
    BottomAppBar(
        containerColor = BlueMSC
    )
    {
        menuItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == indexSelected,
                icon = {
                    Icon(item.icon, contentDescription = item.contentDescription)
                },
                onClick = {
                    item.navigate(navigationHomeScreen)
                    onItemClick(index)
                },
                label = {
                    Text(item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Black,
                    indicatorColor = BlueMSC
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomBottomBarPreview() {
    val navController = rememberNavController()

    CustomBottomBar(
        navigationHomeScreen = navController,
        onItemClick = {},
        indexSelected = 0
    )
}
