package com.around_team.todolist.utils

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ReminderNotificationHelper {
    fun scheduleReminder(context: Context, reminderId: Long, triggerTime: Long) {
        val currentTime = System.currentTimeMillis()
        val delay = triggerTime - currentTime

        if (delay > 0) {
            val inputData = Data.Builder()
                .putLong(ReminderNotificationWorker.REMINDER_ID_KEY, reminderId)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag("reminder_$reminderId")
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    fun cancelReminder(context: Context, reminderId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag("reminder_$reminderId")
    }
}


