package com.around_team.todolist.data.db

import com.around_team.todolist.ui.common.models.Reminder
import javax.inject.Inject

/**
 * Repository class for managing Reminders in the database.
 */
class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
) {
    suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder.toEntity())
    }

    suspend fun getAllReminders(): List<Reminder> {
        return reminderDao.getAllReminders().map { it.toReminder() }
    }

    suspend fun getRemindersByTaskId(taskId: String): List<Reminder> {
        return reminderDao.getRemindersByTaskId(taskId).map { it.toReminder() }
    }

    suspend fun getRemindersByTimeRange(fromTime: Long, toTime: Long): List<Reminder> {
        return reminderDao.getRemindersByTimeRange(fromTime, toTime).map { it.toReminder() }
    }

    suspend fun getReminderById(id: Long): Reminder? {
        return reminderDao.getReminderById(id)?.toReminder()
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder.toEntity())
    }

    suspend fun deleteReminderById(id: Long) {
        reminderDao.deleteReminderById(id)
    }

    suspend fun deleteRemindersByTaskId(taskId: String) {
        reminderDao.deleteRemindersByTaskId(taskId)
    }
}

