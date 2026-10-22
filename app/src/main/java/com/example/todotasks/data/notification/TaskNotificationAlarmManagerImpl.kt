package com.example.todotasks.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.todotasks.data.alarm.AlarmReceiver
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.DAY_TASK_ALARM
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.NAME_TASK_ALARM
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.NOTIFICATION_TASK_ALARM_ID
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.TASK_ID
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

        val pendingIntentId = ("${task.id}_$dayBefore").hashCode()
        val notificationTaskId = ("TASK_ID:${task.id}").hashCode()
        val intent = Intent(context, AlarmReceiver::class.java)
        val dayBeforeText = when (dayBefore) {
            7L -> "Quedan 7 días"
            2L -> "Quedan 2 días"
            1L -> "Queda 1 día"
            else -> ""
        }

        intent.putExtra(NAME_TASK_ALARM, task.tittle)
        intent.putExtra(DAY_TASK_ALARM, dayBeforeText)
        intent.putExtra(TASK_ID, task.id)
        intent.putExtra(NOTIFICATION_TASK_ALARM_ID, notificationTaskId)

        return PendingIntent.getBroadcast(
            context,
            pendingIntentId,
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
            setAlarm(pendingIntent, delay)

        }
    }

    private fun setAlarm(pendingIntent: PendingIntent, delay: Long) {

        // Comprobamos si es API 31
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return
            }
        }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                pendingIntent
            )
    }


    override fun cancel(task: Task) {

        reminders.forEach { daysBefore ->

            val pendingIntent = createPendingIntent(task, daysBefore)

            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
