package com.example.todotasks.data.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todotasks.ui.subTask.list_subtask.SubTaskActivity
import com.example.todotasks.ui.subTask.list_subtask.SubTaskActivity.Companion.EXTRA_TASK_ID
import com.example.todotasks.ui.subTask.list_subtask.SubTaskActivity.Companion.EXTRA_TASK_NAME
import com.example.todotasks.ui.task.TaskActivity

class TaskReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    companion object{
        const val TASK_NAME_WORKER = "taskName"
        const val TASK_ID_WORKER = "taskId"
        const val DAYS_BEFORE_WORKER = "daysBefore"
    }

    override fun doWork(): Result {

        val name = inputData.getString(TASK_NAME_WORKER) ?: return Result.failure()
        val taskId = inputData.getLong(TASK_ID_WORKER, -1L)
        val days = inputData.getLong(DAYS_BEFORE_WORKER, 0)

        val text = when (days) {
            7L -> "Queda 1 semana"
            2L -> "Quedan 2 días"
            1L -> "Queda 1 día"
            else -> ""
        }

        val intent = Intent(applicationContext, SubTaskActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_TASK_NAME, name)
            putExtra(EXTRA_TASK_ID, taskId)
        }

        // Para hacer que al entrar desde 0 y darle hacia atrás vaya a la activity anterior normal (hay que poner en el Manifest cuál es el padre)
        val pendingIntent: PendingIntent = TaskStackBuilder.create(applicationContext).run {
            addParentStack(TaskActivity::class.java)
            addNextIntent(intent)
            getPendingIntent(
                taskId.toInt(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }

        val notification = NotificationCompat.Builder(applicationContext, "task_channel")
            .setContentTitle(name)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        manager.notify(System.currentTimeMillis().toInt(), notification)

        return Result.success()
    }
}