package com.around_team.todolist.ui.screens.reminder.models

import com.around_team.todolist.ui.common.models.Reminder

sealed class ReminderEvent {
    data class SaveReminder(
        val title: String,
        val description: String,
        val triggerTime: Long,
        val taskId: String? = null
    ) : ReminderEvent()

    data class DeleteReminder(val id: Long) : ReminderEvent()
    object LoadReminders : ReminderEvent()
}


