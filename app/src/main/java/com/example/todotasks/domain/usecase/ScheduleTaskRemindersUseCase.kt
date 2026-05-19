package com.example.todotasks.domain.usecase

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.notification.TaskNotificationWorkManager
import javax.inject.Inject

class ScheduleTaskRemindersUseCase @Inject constructor(
    private val scheduler: TaskNotificationWorkManager
){
    operator fun invoke(task: Task){
        scheduler.cancelNotification(task.id)

        if (task.date == null) return

        scheduler.createNotificatiton(task)

    }
}