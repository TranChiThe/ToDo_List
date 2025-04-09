package com.example.todolist.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todolist.presentation.navigation.Screen
import com.example.todolist.presentation.task.TaskItem
import com.example.todolist.presentation.util.TaskEvent
import com.example.todolist.presentation.view_model.AddEditTaskViewModel
import com.example.todolist.presentation.view_model.SearchTaskViewModel
import com.example.todolist.presentation.view_model.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    searchTaskViewModel: SearchTaskViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onTopBarContentChange: (@Composable () -> Unit) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val searchResults = searchTaskViewModel.searchResults.value
    val taskViewModel: TaskViewModel = hiltViewModel()
    val viewModel: AddEditTaskViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    onTopBarContentChange {
        Column(
            modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    coroutineScope.launch(Dispatchers.IO) { searchTaskViewModel.searchTasks(it) }
                },
                placeholder = { Text("Enter task title ...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                singleLine = true,
            )
        }
    }
    Column(
        modifier =
            Modifier.fillMaxSize().padding(top = 16.dp).clickable(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (text.isNotEmpty()) {
            if (searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(searchResults) { task ->
                        TaskItem(
                            task = task,
                            modifier = Modifier,
                            onClick = {
                                navController.navigate(Screen.EditTask.createRoute(task.id))
                            },
                            onFavorite = { taskViewModel.onEvent(TaskEvent.UpdateTask(task)) },
                            onCheckBox = { taskViewModel.onEvent(TaskEvent.UpdateTask(task)) },
                            onDelete = { viewModel.deleteTaskById(task.id) },
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No tasks found",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
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
        }
    }
}
