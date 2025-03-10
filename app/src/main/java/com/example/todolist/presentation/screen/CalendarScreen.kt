package com.example.todolist.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import java.time.Instant.ofEpochMilli
import java.time.LocalDate
import java.time.ZoneId

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val tasks by taskViewModel.taskFlow.collectAsState(initial = emptyList())

    // Get future task time
    val taskDates = tasks
        .filter { it.startTime > System.currentTimeMillis() }
        .map { it.startTime.toLocalDate() }
        .distinct()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        yearRange = 2000..2030,
    )
    val selectedDate = datePickerState.selectedDateMillis?.toLocalDate()
    val tasksForSelectedDay = if (selectedDate != null) {
        tasks.filter { task ->
            task.startTime.toLocalDate() == selectedDate
        }
    } else {
        emptyList()
    }
    val colors = DatePickerDefaults.colors(
        selectedDayContainerColor = Color.Blue,
        todayContentColor = Color.Red,
        dayInSelectionRangeContainerColor = Color.LightGray
    )
    LaunchedEffect(Unit) {
        taskViewModel.loadTasks()
    }

    AppScaffold(
        navController = navController,
        showFab = true // Hiển thị FAB
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            DatePicker(
                state = datePickerState,
                title = { Text(text = "") },
                showModeToggle = true,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    todayContentColor = Color.Red,
                ),
                modifier = modifier
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (selectedDate != null) {
                if (tasksForSelectedDay.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No schedule at this time",
                            fontSize = 20.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn {
                        items(tasksForSelectedDay) { task ->
                            TaskItem(
                                task = task,
                                onFavorite = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                },
                                onCheckBox = {
                                    taskViewModel.onEvent(TaskEvent.UpdateTask(task))
                                },
                                onClick = {
                                    navController.navigate(Screen.EditTask.createRoute(task.id))
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}



@SuppressLint("NewApi")
fun Long.toLocalDate(): LocalDate {
    return ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
