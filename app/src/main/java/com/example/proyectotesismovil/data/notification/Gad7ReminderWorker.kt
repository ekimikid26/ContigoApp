package com.example.proyectotesismovil.data.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class Gad7ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        ContigoNotificationManager.showGad7ReminderNotification(
            applicationContext
        )
        return Result.success()
    }

    companion object {
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<Gad7ReminderWorker>(
                7, TimeUnit.DAYS
            )
                .setInitialDelay(7, TimeUnit.DAYS)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "gad7_reminder",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }
    }
}
