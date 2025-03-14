package com.example.todolist.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class NavItem(val label: String,
                   val icon: ImageVector,
                   val selectedColor: Color,
                   val unselectedColor: Color,
                   val route: String)

val bottomNavItems = listOf(
    NavItem("Home", Icons.Default.Home, Color(0xFF1E88E5), Color(0xFF9E9E9E), "home"),
    NavItem("Search", Icons.Default.Search, Color(0xFF1E88E5), Color(0xFF9E9E9E), "search"),
    NavItem("Calendar", Icons.Default.DateRange, Color(0xFF1E88E5), Color(0xFF9E9E9E), "calendar"),
    NavItem("Favorite", Icons.Default.Favorite, Color(0xFFFF0000), Color(0xFF9E9E9E), "favorite"),
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    if (currentRoute == "addTask") return

    NavigationBar(
        containerColor = Color.White, tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(selected = isSelected, onClick = {
                navController.navigate(item.route) {
                    popUpTo("home") { inclusive = false } // Đảm bảo không bị nhân bản màn hình
                    launchSingleTop = true
                }
            }, icon = {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) item.selectedColor else item.unselectedColor,
                    modifier = Modifier.size(28.dp)
                )
            }, label = {
                Text(
                    text = item.label, fontSize = 15.sp
                )
            })
        }
    }
}