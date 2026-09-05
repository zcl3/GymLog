package com.personal.gymlog.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WaterReminderScheduler {
    private const val NAME = "water_reminder"
    fun schedule(context: Context, intervalMinutes: Long) {
        val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(intervalMinutes.coerceAtLeast(15), TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }
    fun cancel(context: Context) = WorkManager.getInstance(context).cancelUniqueWork(NAME)
}
