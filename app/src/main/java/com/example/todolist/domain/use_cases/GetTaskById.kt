package com.example.todolist.domain.use_cases

import com.example.todolist.domain.model.Task
import com.example.todolist.domain.repositories.TaskRepository

class GetTaskById(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Long): Task? {
        return taskRepository.getTaskById(taskId)
    }
}