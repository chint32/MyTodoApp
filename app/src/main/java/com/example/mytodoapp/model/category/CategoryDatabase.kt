package com.example.mytodoapp.model.category

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mytodoapp.model.todo.TodoDao
import com.example.mytodoapp.model.todo.TodoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CategoryEntity::class], version = 1)
abstract class CategoryDatabase : RoomDatabase(){
    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: CategoryDatabase?= null

        fun getDatabase(context: Context, scope: CoroutineScope): CategoryDatabase {
            return INSTANCE
                ?: synchronized(this){
                    val instance =
                        Room.databaseBuilder(context.applicationContext, CategoryDatabase::class.java, "category_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(
                                CategoryDatabaseCallback(
                                    scope
                                )
                            )
                            .build()
                    INSTANCE = instance
                    instance
                }
        }

        private class CategoryDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                super.onOpen(db)
////                 If you want to keep the data through app restarts,
////                 comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.categoryDao())
//                    }
//                }
//            }
        }

//        suspend fun populateDatabase(categoryDao: CategoryDao) {
//            categoryDao.deleteAll()
//        }
    }
}