package com.example.todolist.domain.use_cases

import com.example.todolist.domain.model.Task
import com.example.todolist.domain.repositories.TaskRepository

class DeleteTask(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(task: Task) = taskRepository.deleteTask(task)
}
