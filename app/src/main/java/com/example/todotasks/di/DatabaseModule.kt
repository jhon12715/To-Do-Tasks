package com.example.todotasks.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todotasks.data.local.room.AppDataBase
import com.example.todotasks.data.local.room.dao.CategoryDao
import com.example.todotasks.data.local.room.dao.SubTaskDao
import com.example.todotasks.data.local.room.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        "AppDataBase"
    )
        .addMigrations(MIGRATION_11_12)
        .addMigrations(MIGRATION_12_13)
        .addMigrations(MIGRATION_13_14)
        .build()

    private val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Crear tabla category
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS category (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            )
        """
            )

            // Crear nueva tabla task con la FK
            database.execSQL(
                """
            CREATE TABLE task_new (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                task TEXT NOT NULL,
                priority INTEGER NOT NULL,
                isCompleted INTEGER NOT NULL,
                date INTEGER,
                category INTEGER,
                FOREIGN KEY(category) REFERENCES category(id)
                    ON DELETE SET NULL
            )
        """
            )

            // Copiar los datos de la tabla antigua
            database.execSQL(
                """
            INSERT INTO task_new (
                id,
                task,
                priority,
                isCompleted,
                date
            )
            SELECT
                id,
                task,
                priority,
                isCompleted,
                date
            FROM task
        """
            )

            // Eliminar tabla antigua
            database.execSQL("DROP TABLE task")

            // Renombrar la nueva
            database.execSQL("ALTER TABLE task_new RENAME TO task")

            // Recrear índices que tuviera task
            database.execSQL(
                """
            CREATE UNIQUE INDEX IF NOT EXISTS index_task_task
            ON task(task)
        """
            )

            database.execSQL(
                """
            CREATE INDEX IF NOT EXISTS index_task_category
            ON task(category)
        """
            )
        }
    }
    private val MIGRATION_12_13 = object : Migration(12, 13) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "DELETE FROM category " +
                        "WHERE id NOT IN ( " +
                        "SELECT MIN(id) " +
                        "FROM category " +
                        "GROUP BY name)"
            )
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_category_name ON category(name)")
        }
    }

    private val MIGRATION_13_14 = object : Migration(13, 14) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE task RENAME COLUMN task to tittle"
            )

            database.execSQL(
                "ALTER TABLE task RENAME COLUMN category to categoryId"
            )

        }
    }

    @Provides
    fun provideTaskDao(db: AppDataBase): TaskDao = db.taskDao()

    @Provides
    fun provideSubTaskDao(db: AppDataBase): SubTaskDao = db.subTaskDao()

    @Provides
    fun provideCategoryDao(db: AppDataBase): CategoryDao = db.categoryDao()
}