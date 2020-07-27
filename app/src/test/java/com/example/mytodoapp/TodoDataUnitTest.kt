package com.example.mytodoapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mytodoapp.model.todo.TodoDao
import com.example.mytodoapp.model.todo.TodoDatabase
import com.example.mytodoapp.model.todo.TodoEntity
import com.example.mytodoapp.view.TodoActivity
import com.example.mytodoapp.viewmodel.ActiveTodoViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TodoDataUnitTest {

    @Test
    fun onePlusOneEqualsTwo(){
        var two = 1 +1
        assertEquals(2, two)
    }

}
