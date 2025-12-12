package com.around_team.todolist.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.around_team.todolist.MainActivity
import com.around_team.todolist.R
import com.around_team.todolist.data.db.ReminderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class ReminderNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderRepository: ReminderRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val reminderId = inputData.getLong(REMINDER_ID_KEY, -1)
                if (reminderId == -1L) {
                    return@withContext Result.failure()
                }

                val reminder = reminderRepository.getReminderById(reminderId)
                if (reminder != null) {
                    showNotification(reminder)
                }
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        }
    }

    private fun showNotification(reminder: com.around_team.todolist.ui.common.models.Reminder) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel
        val channelId = "todo_reminders_channel"
        val channel = NotificationChannel(
            channelId,
            "Todo Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for todo reminders"
        }
        notificationManager.createNotificationChannel(channel)

        // Create intent
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(reminder.title)
            .setContentText(reminder.description)
            .setStyle(NotificationCompat.BigTextStyle().bigText(reminder.description))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(reminder.id.toInt(), notification)
    }

    companion object {
        const val REMINDER_ID_KEY = "reminder_id"
    }
}


