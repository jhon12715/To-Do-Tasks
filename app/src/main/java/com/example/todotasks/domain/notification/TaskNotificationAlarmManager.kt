package com.example.todotasks.domain.notification

import android.app.PendingIntent
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskReminder

interface TaskNotificationAlarmManager {
    fun createPendingIntent(task: Task, dayBefore: TaskReminder): PendingIntent
    fun schedule(task: Task)
    fun cancel(task: Task)
}