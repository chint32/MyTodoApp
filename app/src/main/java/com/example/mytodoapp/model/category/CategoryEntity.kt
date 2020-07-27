package com.example.mytodoapp.model.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryEntity(@PrimaryKey var category: String = "daily")