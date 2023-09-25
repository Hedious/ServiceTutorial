package com.example.servicetutorial

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.servicetutorial.notification.buildNotification


class ForegroundService : Service() {

    companion object {
        fun startService(context: Context, message: String) {
            val start = Intent(context, ForegroundService::class.java)
            ContextCompat.startForegroundService(context, start)
        }

        fun stopService(context: Context) {
            val end = Intent(context, ForegroundService::class.java)
            context.stopService(end)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification(this, "Titles", "content")
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}