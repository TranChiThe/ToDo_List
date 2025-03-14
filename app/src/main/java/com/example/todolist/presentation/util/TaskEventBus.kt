package com.example.todolist.presentation.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object TaskEventBus {
    private val _eventFlow = MutableSharedFlow<Unit>(replay = 1)
    val eventFlow: SharedFlow<Unit> = _eventFlow

    suspend fun sendEvent() {
        _eventFlow.emit(Unit)
    }
}
