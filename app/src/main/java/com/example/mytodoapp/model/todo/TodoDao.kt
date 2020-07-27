package com.example.mytodoapp.model.todo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mytodoapp.model.todo.TodoEntity

@Dao
interface TodoDao {
    @Query("SELECT * from todo_table where isCompleted = 0")
    fun getListOfActiveTodos(): LiveData<List<TodoEntity>>

    @Query("SELECT * from todo_table where isCompleted = 1")
    fun getListOfCompletedTodos(): LiveData<List<TodoEntity>>

    @Update
    fun updateTodo(todoEntity: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todoEntity: TodoEntity)

    @Query("DELETE FROM todo_table where isCompleted = 1")
    suspend fun deleteAllCompleted()

    @Delete
    suspend fun deleteTask(todoEntity: TodoEntity)

}