package com.example.mytodoapp.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.mytodoapp.model.category.CategoryDao
import com.example.mytodoapp.model.category.CategoryEntity
import com.example.mytodoapp.model.todo.TodoDao
import com.example.mytodoapp.model.todo.TodoEntity

class TodoRepository(private val todoDao: TodoDao, private val categoryDao: CategoryDao) {

    val listOfTodos: LiveData<List<TodoEntity>> = todoDao.getListOfActiveTodos()
    val listOfCompletedTodos: LiveData<List<TodoEntity>> = todoDao.getListOfCompletedTodos()

    val listOfCategories: LiveData<List<CategoryEntity>> = categoryDao.getListOfCategories()

    @WorkerThread
    fun saveTodo(todoEntity: TodoEntity){
        todoDao.insertTodo(todoEntity)
    }

    @WorkerThread
    fun saveCategory(categoryEntity: CategoryEntity){
        categoryDao.insertCategory(categoryEntity)
    }

    @WorkerThread
    suspend fun deleteTodo(todoEntity: TodoEntity){
        todoDao.deleteTask(todoEntity)
    }

    @WorkerThread
    suspend fun deleteAllCompletedTasks(){
        todoDao.deleteAllCompleted()
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity){
        categoryDao.deleteCategory(categoryEntity)
    }
}