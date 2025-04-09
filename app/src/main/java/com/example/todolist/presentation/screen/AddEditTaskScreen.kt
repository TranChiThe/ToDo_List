package com.example.todolist.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.todolist.presentation.util.AddEditTaskEvent
import com.example.todolist.presentation.view_model.AddEditTaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskId: Long? = null,
    viewModel: AddEditTaskViewModel = hiltViewModel(),
    onTopBarContentChange: (@Composable () -> Unit, @Composable () -> Unit, @Composable () -> Unit) -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    val title = viewModel.title.value
    val content = viewModel.content.value
    val startTime = viewModel.startTime.value
    val endTime = viewModel.endTime.value
    val favorite = viewModel.favorite.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // State cho Date/Time Picker
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startTime)
    val timePickerState =
        rememberTimePickerState(
            initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
        )
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isStartTime by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    onTopBarContentChange(
        { Text(if (taskId == null) "Add Task" else "Edit Task") },
        {
            IconButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        isLoading = true
                        delay(2000)
                    }
                    viewModel.onEvent(AddEditTaskEvent.saveTask)
                    isLoading = false
                    navController.popBackStack()
                },
                enabled = title.isNotBlank(),
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = if (title.isNotBlank()) Color.Blue else Color.Gray,
                )
            }
        },
        {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
    )

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTaskById(taskId)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
                .imePadding()
                .clickable(onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }, indication = null, interactionSource = remember { MutableInteractionSource() }),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TimeSelectionSection(
            startTime = startTime,
            endTime = endTime,
            onStartTimeClick = {
                showDatePicker = true
                isStartTime = true
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            onEndTimeClick = {
                showDatePicker = true
                isStartTime = false
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        )

        TaskInputSection(
            taskTitle = title,
            taskContent = content,
            onTaskTitleChange = { viewModel.onEvent(AddEditTaskEvent.EnteredTitle(it)) },
            onTaskContentChange = { viewModel.onEvent(AddEditTaskEvent.EnteredContent(it)) },
            onCancel = {},
            onSave = {},
            isFavorite = favorite,
            onFavorite = { viewModel.onEvent(AddEditTaskEvent.ToggleFavorite) },
            onDelete = {
                taskId?.let {
                    viewModel.deleteTaskById(it)
                    navController.popBackStack()
                }
            },
        )

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            showTimePicker = true
                        },
                    ) {
                        Text("Next")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Time Picker Dialog
        if (showTimePicker) {
            DatePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val calendar =
                                Calendar.getInstance().apply {
                                    timeInMillis =
                                        datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    set(Calendar.MINUTE, timePickerState.minute)
                                }
                            val selectedTime = calendar.timeInMillis
                            if (isStartTime) {
                                viewModel.onEvent(AddEditTaskEvent.SetStartTime(selectedTime))
                            } else {
                                viewModel.onEvent(AddEditTaskEvent.SetEndTime(selectedTime))
                            }
                            showTimePicker = false
                        },
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                },
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}

@Composable
fun TimeSelectionSection(
    startTime: Long,
    endTime: Long,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
            Text(
                text = "Task Timing",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Start: ${
                        SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault(),
                        ).format(startTime)
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                )
                IconButton(onClick = onStartTimeClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Start Time",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "End: ${
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                            endTime,
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                )
                IconButton(onClick = onEndTimeClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit End Time",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
fun TaskInputSection(
    taskTitle: String,
    taskContent: String,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onTaskTitleChange: (String) -> Unit,
    onTaskContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
                Row {
                    IconButton(onClick = { onFavorite() }) {
                        Icon(
                            imageVector =
                                if (isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Filled.FavoriteBorder
                                },
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                        )
                    }

                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            tint = Color.Red,
                        )
                    }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text("Confirm deletion") },
                            text = { Text("Are you sure you want to delete this task?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDeleteDialog = false
                                        onDelete()
                                    },
                                ) {
                                    Text("Delete", color = Color.Red)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteDialog = false }) {
                                    Text("Cancel")
                                }
                            },
                        )
                    }
                }
            }
            OutlinedTextField(
                value = taskTitle,
                onValueChange = onTaskTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Task Title") },
                placeholder = { Text("Enter title") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
            )

            OutlinedTextField(
                value = taskContent,
                onValueChange = onTaskContentChange,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                label = { Text("Task Content") },
                placeholder = { Text("Enter content") },
                maxLines = 5,
                shape = RoundedCornerShape(8.dp),
            )
        }
    }
}
