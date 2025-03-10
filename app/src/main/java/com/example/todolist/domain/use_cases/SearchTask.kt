package com.example.todolist.domain.use_cases

import com.example.todolist.domain.repositories.TaskRepository

class SearchTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(searchTerm: String) = taskRepository.searchTasks(searchTerm)
}