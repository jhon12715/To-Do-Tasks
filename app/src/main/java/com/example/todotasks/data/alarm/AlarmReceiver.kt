package com.example.todotasks.data.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todotasks.data.core.NotificationConfig.Companion.CHANNEL_TASK

class AlarmReceiver : BroadcastReceiver() {
    companion object{
        const val NAME_TASK_ALARM = "NAME_TASK_ALARM"
        const val DAY_TASK_ALARM = "DAY_TASK_ALARM"
        const val ID_NOTIFICATION_TASK_ALARM = "ID_NOTIFICATION_TASK_ALARM"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            //val notificationManager =
            //it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //val runnerNotifier = RunnerNotifier(notificationManager, it)
            //runnerNotifier.showNotification(
            val name = intent?.getStringExtra(NAME_TASK_ALARM) ?: ""
            val dayBeforeText = intent?.getStringExtra(DAY_TASK_ALARM) ?: ""
            val idNotificationTask = intent?.getIntExtra(ID_NOTIFICATION_TASK_ALARM,0)

            val notification = NotificationCompat.Builder(context, CHANNEL_TASK)
                .setContentTitle(name)
                .setContentText(dayBeforeText)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.notify(idNotificationTask!!, notification)

        }
    }
}