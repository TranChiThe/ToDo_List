package com.example.todolist.presentation.util

sealed class AddEditTaskEvent {
    data class EnteredTitle(val title: String) : AddEditTaskEvent()
    data class EnteredContent(val content: String) : AddEditTaskEvent()
    data class SetStartTime(val time: Long) : AddEditTaskEvent()
    data class SetEndTime(val time: Long) : AddEditTaskEvent()
    object ToggleFavorite : AddEditTaskEvent()
    object saveTask : AddEditTaskEvent()
    object cancelTask : AddEditTaskEvent()

}