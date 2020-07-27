package com.example.mytodoapp.model.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoEntity (
    @PrimaryKey @ColumnInfo(name = "title") val title: String,
    val description: String, var isCompleted: Boolean = false,
    var priority: String, val dueDate: String, val dueTime: String, var timeLeft: String,
    var reminder: String = "No Reminder", var category: String = ""
)