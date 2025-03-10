package com.example.todolist.presentation.view_model

import com.example.todolist.domain.model.Task


sealed class TaskEvent {
    data class DeleteTask(val task: Task) : TaskEvent()
    data class UpdateTask(val task: Task) : TaskEvent()
    object RestoreTask : TaskEvent()

}