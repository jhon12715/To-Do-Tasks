package com.example.todotasks.di

import com.example.todotasks.data.repository.DataStoreRepositoryImpl
import com.example.todotasks.data.repository.TaskRepositoryImpl
import com.example.todotasks.domain.repository.DataStoreRepository
import com.example.todotasks.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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