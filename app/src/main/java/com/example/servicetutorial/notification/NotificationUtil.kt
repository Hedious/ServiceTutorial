package com.example.servicetutorial.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.servicetutorial.MainActivity
import com.example.servicetutorial.R


fun buildNotification(
    context: Context,
    title: String,
    content: String
): Notification {

    val notificationChannelId = context.packageName + ".channel"
    createNotificationChannel(context, notificationChannelId)

    val bigTextStyle = NotificationCompat.BigTextStyle()
    bigTextStyle.bigText(content)
    val notiIntent = Intent(context, MainActivity::class.java)
    val pendingIntent =
        PendingIntent.getActivity(context, 0, notiIntent, PendingIntent.FLAG_IMMUTABLE)
    return NotificationCompat.Builder(context, notificationChannelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentText(content)
        .setStyle(bigTextStyle)
        .setContentIntent(pendingIntent)
        .build()
}

fun createNotificationChannel(context: Context, notificationChannelId: String) {
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val appName = context.getString(R.string.app_name)
        val channel =
            NotificationChannel(
                notificationChannelId,
                appName,
                NotificationManager.IMPORTANCE_HIGH
            )
        notificationManager.createNotificationChannel(channel)
    }
}




