package com.example.todotasks.di

import android.app.Application
import android.content.Context
import androidx.room.Room
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
            .build()
    }

    @Provides
    fun provideMyDao(db: AppDataBase): TaskDao = db.taskDao()

    @Provides
    fun provideMyDaoo(db: AppDataBase): SubTaskDao = db.subTaskDao()
}