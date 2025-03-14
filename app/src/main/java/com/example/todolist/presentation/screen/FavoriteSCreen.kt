package com.example.todolist.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todolist.presentation.navigation.Screen
import com.example.todolist.presentation.task.TaskItem
import com.example.todolist.presentation.util.AppScaffold
import com.example.todolist.presentation.view_model.TaskEvent
import com.example.todolist.presentation.view_model.TaskViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskViewModel: TaskViewModel = hiltViewModel(),
) {
    val tasks by taskViewModel.taskFlow.collectAsState(initial = emptyList())
    LaunchedEffect(Unit) {
        taskViewModel.getFavoriteTask(true)
    }
    AppScaffold(
        navController = navController,
        showFab = true, // Hiển thị FAB
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Task Favorite",
                fontSize = 30.sp,
                fontWeight = FontWeight(300),
            )
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No task",
                            fontSize = 20.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                val groupedTasks = groupTasksByDate(tasks)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    val orderedKeys = listOf("Today", "Yesterday", "One week ago", "Older")
                    orderedKeys.forEach { key ->
                        groupedTasks[key]?.let { taskList ->
                            item {
                                Text(
                                    text = key,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                            }
                            items(taskList, key = { it.id }) { task ->
                                TaskItem(task = task, modifier = Modifier, onClick = {
                                    navController.navigate(Screen.EditTask.createRoute(task.id))
                                }, onFavorite = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                }, onCheckBox = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                })
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
