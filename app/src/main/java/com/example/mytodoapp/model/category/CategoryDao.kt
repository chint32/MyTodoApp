package com.example.mytodoapp.model.category

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mytodoapp.model.todo.TodoEntity

@Dao
interface CategoryDao {
    @Query("SELECT * from category_table")
    fun getListOfCategories(): LiveData<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM category_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)


}