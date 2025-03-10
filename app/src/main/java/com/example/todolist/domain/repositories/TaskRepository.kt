package com.example.todolist.domain.repositories

import com.example.todolist.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<Task>>
    suspend fun getTaskById(taskId: Long): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun searchTasks(searchTerm: String): List<Task>
    suspend fun deleteTaskById(taskId: Long)
    suspend fun getFavoriteTask(isFavorite: Boolean): Flow<List<Task>>
}