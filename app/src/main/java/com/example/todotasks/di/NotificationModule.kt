package com.example.todotasks.di

import com.example.todotasks.data.notification.TaskNotificationAlarmManagerImpl
import com.example.todotasks.data.notification.TaskNotificationWorkManagerImpl
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import com.example.todotasks.domain.notification.TaskNotificationWorkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModuleModule {

    @Binds
    abstract fun provideWorkScheduler(
        impl: TaskNotificationWorkManagerImpl
    ): TaskNotificationWorkManager

    @Binds
    abstract fun provideAlarmScheduler(
        impl: TaskNotificationAlarmManagerImpl
    ): TaskNotificationAlarmManager
}
