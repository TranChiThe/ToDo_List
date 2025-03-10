package com.example.todolist.domain.use_cases

import com.example.todolist.domain.repositories.TaskRepository

class DeleteTaskById(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long) = taskRepository.deleteTaskById(taskId)
}