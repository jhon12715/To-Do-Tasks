package com.example.todotasks.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todotasks.data.local.room.converters.PriorityConverter
import com.example.todotasks.data.local.room.dao.CategoryDao
import com.example.todotasks.data.local.room.dao.SubTaskDao
import com.example.todotasks.data.local.room.dao.TaskDao
import com.example.todotasks.data.local.room.entities.CategoryEntity
import com.example.todotasks.data.local.room.entities.SubTaskEntity
import com.example.todotasks.data.local.room.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, SubTaskEntity::class, CategoryEntity::class], version = 13, exportSchema = true
)
@TypeConverters(PriorityConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
    abstract fun categoryDao(): CategoryDao

}

