package com.example.todotasks.domain.notification

import com.example.todotasks.domain.model.Task

interface TaskNotificationWorkManager {
    fun createNotificatiton(task: Task)
    fun cancelNotification(taskId: Long)
}