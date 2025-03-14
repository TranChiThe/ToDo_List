package com.example.todolist.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todolist.presentation.navigation.Screen

@Composable
fun AppScaffold(
    navController: NavHostController,
    showFab: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        if (showFab) {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddTask.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                })
        }
    }, floatingActionButtonPosition = FabPosition.End, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
            CustomSnackbar(data)
        }
    }) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun CustomSnackbar(data: SnackbarData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Snackbar(
            action = {
                Row {
                    TextButton(onClick = { data.performAction() }) {
                        Text("Undo", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { data.dismiss() }) {
                        Text("Cancel", color = Color.White)
                    }
                }
            }, modifier = Modifier.padding(16.dp)
        ) {
            Text(data.visuals.message)
        }
    }
}