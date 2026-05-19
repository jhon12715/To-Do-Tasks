package com.example.todotasks.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todotasks.data.alarm.AlarmReceiver
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.DAY_TASK_ALARM
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.ID_NOTIFICATION_TASK_ALARM
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.NAME_TASK_ALARM
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class TaskNotificationAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TaskNotificationAlarmManager {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val reminders = listOf(7L, 2L, 1L)

    override fun createPendingIntent(task: Task, dayBefore: Long): PendingIntent {

        val idNotificationTask = ("${task.id}_$dayBefore").hashCode()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "TASK_REMINDER_$idNotificationTask"
        }
        intent.putExtra(NAME_TASK_ALARM, task.task)
        val dayBeforeText = when (dayBefore) {
            7L -> "Quedan 7 días"
            2L -> "Quedan 2 días"
            1L -> "Queda 1 día"
            else -> ""
        }

        intent.putExtra(DAY_TASK_ALARM, dayBeforeText)
        intent.putExtra(ID_NOTIFICATION_TASK_ALARM, idNotificationTask)

        return PendingIntent.getBroadcast(
            context,
            idNotificationTask,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun schedule(task: Task) {

        val date = task.date ?: return

        reminders.forEach { daysBefore ->

            val trigger = date.minusDays(daysBefore).atStartOfDay()
            val now = LocalDateTime.now()

            val delay = ChronoUnit.MILLIS.between(now, trigger)

            if (delay <= 0) return@forEach

            val pendingIntent = createPendingIntent(task, daysBefore)

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                pendingIntent
            )
        }
    }


    override fun cancel(task: Task) {

        reminders.forEach { daysBefore ->

            val pendingIntent = createPendingIntent(task, daysBefore)

            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
