package com.example.todotasks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todotasks.data.database.room.AppDataBase
import com.example.todotasks.data.database.room.dao.CategoryDao
import com.example.todotasks.data.database.room.dao.SubTaskDao
import com.example.todotasks.data.database.room.dao.TaskDao
import com.example.todotasks.data.repository.DataStoreRepositoryImpl
import com.example.todotasks.data.repository.TaskRepositoryImpl
import com.example.todotasks.domain.repository.DataStoreRepository
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

    @Binds
    abstract fun bindSettingsRepository(
        impl: DataStoreRepositoryImpl
    ): DataStoreRepository
}