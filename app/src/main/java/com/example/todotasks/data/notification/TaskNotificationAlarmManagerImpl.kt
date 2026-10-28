package com.example.todotasks.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.todotasks.R
import com.example.todotasks.data.alarm.AlarmReceiver
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.DAY_TASK_ALARM
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.NAME_TASK_ALARM
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.NOTIFICATION_TASK_ALARM_ID
import com.example.todotasks.data.alarm.AlarmReceiver.Companion.TASK_ID
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskReminder
import com.example.todotasks.domain.notification.TaskNotificationAlarmManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class TaskNotificationAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TaskNotificationAlarmManager {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val reminders = TaskReminder.values()

    override fun createPendingIntent(task: Task, dayBefore: TaskReminder): PendingIntent {

        val pendingIntentId: Int = ("${task.id}_$dayBefore").hashCode()
        val notificationTaskId: Int = ("TASK_ID:${task.id}").hashCode()
        val intent = Intent(context, AlarmReceiver::class.java)

        val dayBeforeText: String = context.getString(getTaskReminderTextResource(dayBefore))

        intent.apply {
            putExtra(NAME_TASK_ALARM, task.tittle)
            putExtra(DAY_TASK_ALARM, dayBeforeText)
            putExtra(TASK_ID, task.id)
            putExtra(NOTIFICATION_TASK_ALARM_ID, notificationTaskId)
        }

        return PendingIntent.getBroadcast(
            context,
            pendingIntentId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getTaskReminderTextResource(daysBefore: TaskReminder): Int = when (daysBefore) {
        TaskReminder.SEVEN_DAYS -> R.string.task_reminder_7_days
        TaskReminder.TWO_DAYS -> R.string.task_reminder_2_days
        TaskReminder.ONE_DAY -> R.string.task_reminder_1_day
    }

    override fun schedule(task: Task) {

        val date = task.date ?: return

        reminders.forEach { daysBefore ->

            val trigger = date.minusDays(daysBefore.daysBefore).atStartOfDay()
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
