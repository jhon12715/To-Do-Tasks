package com.example.todotasks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todotasks.data.database.converters.PriorityConverter
import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity

@Database(entities = [TaskEntity::class, SubTaskEntity::class], version = 11, exportSchema = false)
@TypeConverters(PriorityConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao

    val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
                    "PRIMARY KEY(`id`))")
        }
    }

}

