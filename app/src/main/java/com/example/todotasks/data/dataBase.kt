package com.example.todotasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity

@Database(entities = [TaskEntity::class, SubTaskEntity::class], version = 1)
abstract class dataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
}