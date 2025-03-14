package com.example.todolist.domain.use_cases

import com.example.todolist.domain.repositories.TaskRepository

class GetFavoriteTask(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(isFavorite: Boolean) = taskRepository.getFavoriteTask(isFavorite)
}