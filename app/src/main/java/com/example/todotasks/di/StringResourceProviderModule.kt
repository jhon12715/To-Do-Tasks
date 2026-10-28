package com.example.todotasks.di

import com.example.todotasks.ui.common.string_resource.AndroidStringProvider
import com.example.todotasks.ui.common.string_resource.StringProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class StringResourceProviderModule {

    @Binds
    abstract fun bindStringResource(
        impl: AndroidStringProvider
    ): StringProvider
}