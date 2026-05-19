package com.example.todotasks.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todotasks.data.database.AppDataBase
import com.example.todotasks.data.database.dao.SubTaskDao
import com.example.todotasks.data.database.dao.TaskDao
import com.example.todotasks.data.repository.TaskRepositoryImpl
import com.example.todotasks.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMyRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {


        return Room.databaseBuilder(context, AppDataBase::class.java, "AppDataBase")
            .fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_1_10)
            .addMigrations(MIGRATION_10_11)
            .build()
    }

    val MIGRATION_1_10 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                "ALTER TABLE task ADD COLUMN dueDate INTEGER"
            )
        }
    }

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {

            // Crear nueva tabla con la columna "date"
            db.execSQL("""
            CREATE TABLE task_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                priority INTEGER NOT NULL,
                task TEXT NOT NULL,
                isCompleted INTEGER NOT NULL,
                date INTEGER
            )
        """)

            // Copiar datos de la tabla antigua
            db.execSQL("""
            INSERT INTO task_new (id, priority, task, isCompleted, date)
            SELECT id, priority, task, isCompleted, dueDate
            FROM task
        """)

            // Borrar tabla antigua
            db.execSQL("DROP TABLE task")

            // Renombrar nueva tabla
            db.execSQL("ALTER TABLE task_new RENAME TO task")
        }
    }

    @Provides
    fun provideTaskDao(db: AppDataBase): TaskDao = db.taskDao()

    @Provides
    fun provideSubTaskDao(db: AppDataBase): SubTaskDao = db.subTaskDao()
}