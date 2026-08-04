package com.example.todotasks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.todotasks.domain.model.SettingsApp
import com.example.todotasks.domain.model.SettingsAppSerializer
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
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<SettingsApp> = DataStoreFactory.create(
        serializer = SettingsAppSerializer,
        produceFile = { context.dataStoreFile("settingsApp.json") }
    )
}