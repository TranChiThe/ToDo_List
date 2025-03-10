package com.example.todolist.domain.use_cases

import com.example.todolist.domain.repositories.TaskRepository

class GetAllTask(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() = taskRepository.getAllTask()
}