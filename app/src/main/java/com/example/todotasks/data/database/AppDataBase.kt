package com.example.todotasks.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todotasks.data.database.converters.PriorityConverter
import com.example.todotasks.data.database.dao.CategoryDao
import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.database.entities.CategoryEntity
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity

@Database(
    entities = [TaskEntity::class, SubTaskEntity::class, CategoryEntity::class], version = 13, exportSchema = true
)
@TypeConverters(PriorityConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
    abstract fun categoryDao(): CategoryDao

}

