package com.example.todolist.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.presentation.screen.AddEditTaskScreen
import com.example.todolist.presentation.screen.CalendarScreen
import com.example.todolist.presentation.screen.FavoriteScreen
import com.example.todolist.presentation.screen.HomeScreen
import com.example.todolist.presentation.screen.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val topBarContent = remember { mutableStateOf<@Composable () -> Unit>({ Text("Task List") }) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showFab = currentRoute !in listOf(Screen.AddTask.route, Screen.EditTask.route)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { topBarContent.value() },
                colors = TopAppBarDefaults.topAppBarColors(Color.White),
            )
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTask.route) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp),
                        )
                    },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = { BottomNavigationBar(navController) },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            navController = navController,
            startDestination = "home",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300),
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 300),
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 300),
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300),
                )
            },
        ) {
            composable(Screen.Home.route) {
                topBarContent.value = { Text("Home") }
                HomeScreen(navController = navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    navController = navController,
                    onTopBarContentChange = { content -> topBarContent.value = content },
                )
            }
            composable(Screen.Calendar.route) {
                topBarContent.value = { Text("Calendar") }
                CalendarScreen(navController = navController)
            }
            composable(Screen.Favorite.route) {
                topBarContent.value = { Text("Favorites") }
                FavoriteScreen(navController = navController)
            }
            composable(Screen.AddTask.route) {
                topBarContent.value = { Text("Add Task") }
                AddEditTaskScreen(navController = navController, taskId = null)
            }
            composable(
                route = Screen.EditTask.route,
                arguments = listOf(navArgument("taskId") { type = NavType.LongType }),
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getLong("taskId")
                topBarContent.value = { Text("Edit Task") }
                AddEditTaskScreen(navController = navController, taskId = taskId)
            }
        }
    }
}
