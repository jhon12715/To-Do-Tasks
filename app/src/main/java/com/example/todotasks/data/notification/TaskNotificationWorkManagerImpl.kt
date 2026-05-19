package com.example.todotasks.data.notification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todotasks.data.worker.TaskReminderWorker
import com.example.todotasks.data.worker.TaskReminderWorker.Companion.DAYS_BEFORE_WORKER
import com.example.todotasks.data.worker.TaskReminderWorker.Companion.TASK_ID_WORKER
import com.example.todotasks.data.worker.TaskReminderWorker.Companion.TASK_NAME_WORKER
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.notification.TaskNotificationWorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskNotificationWorkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TaskNotificationWorkManager {

    override fun createNotificatiton(task: Task) {
        val date = task.date ?: return

        val reminders = listOf(7L, 2L, 1L)

        reminders.forEach { daysBefore ->

            val trigger = date.minusDays(daysBefore).atStartOfDay()
            val now = LocalDateTime.now()

            val delay = ChronoUnit.MILLIS.between(now, trigger)

            if (delay <= 0) return@forEach

            val work = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        TASK_ID_WORKER to task.id,
                        TASK_NAME_WORKER to task.task,
                        DAYS_BEFORE_WORKER to daysBefore
                    )
                )
                .addTag("task_${task.id}") // 🔥 clave para cancelar
                .build()

            WorkManager.getInstance(context).enqueue(work)
        }
    }

    override fun cancelNotification(taskId: Long) {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("task_$taskId")
    }
}