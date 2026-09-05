package com.personal.gymlog.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.personal.gymlog.R

class WaterReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) manager.createNotificationChannel(NotificationChannel(CHANNEL, "喝水提醒", NotificationManager.IMPORTANCE_DEFAULT))
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("GymLog 喝水提醒")
            .setContentText("起来喝一杯水吧")
            .setAutoCancel(true)
            .build()
        manager.notify(NOTIFICATION_ID, notification)
        return Result.success()
    }
    companion object { const val CHANNEL = "water_reminder"; const val NOTIFICATION_ID = 1001 }
}
