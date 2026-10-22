package com.example.todotasks.data.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todotasks.data.core.NotificationConfig.Companion.CHANNEL_TASK
import com.example.todotasks.ui.subTask.subtask_list.SubTaskActivity

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NAME_TASK_ALARM = "NAME_TASK_ALARM"
        const val DAY_TASK_ALARM = "DAY_TASK_ALARM"
        const val TASK_ID = "TASK_ID"
        const val NOTIFICATION_TASK_ALARM_ID = "NOTIFICATION_TASK_ALARM_ID"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            //val notificationManager =
            //it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //val runnerNotifier = RunnerNotifier(notificationManager, it)
            //runnerNotifier.showNotification(
            val taskName = intent?.getStringExtra(NAME_TASK_ALARM) ?: ""
            val dayBeforeText = intent?.getStringExtra(DAY_TASK_ALARM) ?: ""
            val taskId = intent?.getLongExtra(TASK_ID, 0) ?: 0L
            val notificationTaskId = intent?.getIntExtra(NOTIFICATION_TASK_ALARM_ID, 0) ?: 0

            val notification = NotificationCompat.Builder(context, CHANNEL_TASK)
                .setContentTitle(taskName)
                .setContentText(dayBeforeText)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntent(context, taskId, taskName, notificationTaskId))
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.notify(notificationTaskId!!, notification)

        }
    }

    private fun getPendingIntent(
        context: Context,
        taskId: Long,
        taskName: String,
        notificationTaskId: Int
    ): PendingIntent {
        val intent = Intent(context, SubTaskActivity::class.java).apply {
            putExtra(SubTaskActivity.EXTRA_TASK_ID, taskId)
            putExtra(SubTaskActivity.EXTRA_TASK_NAME, taskName)
        }

        val stackBuilder = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent)

        return stackBuilder.getPendingIntent(
            notificationTaskId,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}