package com.example.mytodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.mytodoapp.model.category.CategoryDatabase
import com.example.mytodoapp.model.category.CategoryEntity
import com.example.mytodoapp.model.todo.TodoDatabase
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActiveTodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoRepository: TodoRepository
    val listOfCategories: LiveData<List<CategoryEntity>>
    val listOfTodos: LiveData<List<TodoEntity>>
    val listOfCOmpletedTodos: LiveData<List<TodoEntity>>

    init {
        val todoDao = TodoDatabase.getDatabase(application, viewModelScope).todoDao()
        val categoryDao = CategoryDatabase.getDatabase(application, viewModelScope).categoryDao()
        todoRepository = TodoRepository(todoDao, categoryDao)
        listOfTodos = todoRepository.listOfTodos
        listOfCOmpletedTodos = todoRepository.listOfCompletedTodos
        listOfCategories = todoRepository.listOfCategories
    }


    fun saveTodo(todo: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.saveTodo(todo)
    }

    fun saveCategory(category: CategoryEntity) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.saveCategory(category)
    }

    fun deleteAllCompletedTasks() = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.deleteAllCompletedTasks()
    }

    fun deleteCategory(category: CategoryEntity) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.deleteCategory(category)
    }

    fun deleteTodo(todo: TodoEntity) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.deleteTodo(todo)
    }


}
