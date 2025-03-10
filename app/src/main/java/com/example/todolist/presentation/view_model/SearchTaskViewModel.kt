package com.example.todolist.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Task
import com.example.todolist.domain.use_cases.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {
    private val _searchResults = mutableStateOf<List<Task>>(emptyList())
    val searchResults: State<List<Task>> = _searchResults

    fun searchTasks(searchTerm: String) {
        viewModelScope.launch {
            _searchResults.value = taskUseCases.searchTask(searchTerm)
        }
    }
}