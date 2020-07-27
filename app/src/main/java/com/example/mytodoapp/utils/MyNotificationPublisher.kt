package com.example.mytodoapp.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.mytodoapp.R
import com.example.mytodoapp.view.TodoActivity


class MyNotificationPublisher : BroadcastReceiver() {

    val NOTIFICATION_CHANNEL_ID = "10001"
    val default_notification_channel_id = "default"

    companion object {
        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        val NOTIFICATION_CHANNEL_ID = "10001"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification =
            intent.getParcelableExtra(NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        assert(notificationManager != null)
        notificationManager.notify(id, notification)
//        scheduleNotification(context, getNotification(context, "Todo: Pass in todo info"), 5000)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(context: Context, notification: Notification, delay: Long) {
        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        var futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        assert(alarmManager != null)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }

    private fun getNotification(context: Context, content: String): Notification {

        val resultIntent = Intent(context, TodoActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            context,
            1,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder =
            NotificationCompat.Builder(context, default_notification_channel_id)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_add_black_24dp)
        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(resultPendingIntent)
        return builder.build()
    }
}