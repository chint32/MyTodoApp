package com.example.mytodoapp.model.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TodoEntity::class], version = 10)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun todoDao(): TodoDao

    companion object{
        @Volatile
        private var INSTANCE: TodoDatabase?= null

        fun getDatabase(context: Context, scope: CoroutineScope): TodoDatabase {
            return INSTANCE
                ?: synchronized(this){
                val instance =
                    Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, "todo_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(
                            TodoDatabaseCallback(
                                scope
                            )
                        )
                        .build()
                INSTANCE = instance
                instance
            }
        }

        private class TodoDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                super.onOpen(db)
//                // If you want to keep the data through app restarts,
//                // comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.todoDao())
//                    }
//                }
//            }
        }

//        suspend fun populateDatabase(todoDao: TodoDao) {
//            todoDao.deleteAll()
//        }
    }
}