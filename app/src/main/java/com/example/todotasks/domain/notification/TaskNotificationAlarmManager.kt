package com.example.todotasks.domain.notification

import android.app.PendingIntent
import com.example.todotasks.domain.model.Task

interface TaskNotificationAlarmManager {
    fun createPendingIntent(task: Task, dayBefore: Long): PendingIntent
    fun schedule(task: Task)
    fun cancel(task: Task)
}