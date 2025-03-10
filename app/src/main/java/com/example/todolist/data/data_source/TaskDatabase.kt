package com.example.todolist.data.data_source

import android.content.Context
import com.example.todolist.domain.model.MyObjectBox
import dagger.hilt.android.qualifiers.ApplicationContext
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskDatabase @Inject constructor(@ApplicationContext context: Context) {
    val boxStore: BoxStore = MyObjectBox.builder()
        .androidContext(context.applicationContext)
        .build()
}
