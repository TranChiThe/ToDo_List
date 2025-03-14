package com.example.todolist.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Task
import com.example.todolist.domain.use_cases.TaskUseCases
import com.example.todolist.presentation.util.TaskEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel
    @Inject
    constructor(
        private val taskUseCases: TaskUseCases,
    ) : ViewModel() {
        private val _taskFlow = MutableStateFlow<List<Task>>(emptyList())
        val taskFlow: StateFlow<List<Task>> = _taskFlow

        private val _favoriteTasksFlow = MutableStateFlow<List<Task>>(emptyList())
        val favoriteTasksFlow: StateFlow<List<Task>> = _favoriteTasksFlow
        private var job: Job? = null
        private var deleteTask: Task? = null

        init {
            loadTasks()
            observeTaskEvents()
        }

        private fun observeTaskEvents() {
            viewModelScope.launch {
                TaskEventBus.eventFlow.collect {
                    loadTasks()
                }
            }
        }

        fun loadTasks() {
            job?.cancel()
            job =
                viewModelScope.launch {
                    taskUseCases.getAllTask().collect { tasks ->
                        _taskFlow.value = tasks
                    }
                }
        }

        fun getFavoriteTask(isFavorite: Boolean) {
            job?.cancel()
            job =
                viewModelScope.launch {
                    taskUseCases.getFavoriteTask(isFavorite).collect { tasks ->
                        _taskFlow.value = tasks
                    }
                }
        }

        fun onEvent(event: TaskEvent) {
            when (event) {
                is TaskEvent.DeleteTask -> {
                    viewModelScope.launch {
                        taskUseCases.deleteTask(event.task)
                        loadTasks()
                    }
                    deleteTask = event.task
                }

                is TaskEvent.UpdateTask -> {
                    viewModelScope.launch {
                        delay(500)
                        taskUseCases.updateTask(event.task)
                    }
                }

                is TaskEvent.RestoreTask -> {
                    viewModelScope.launch {
                        taskUseCases.addTask(deleteTask!!)
                    }
                }
            }
        }
    }
