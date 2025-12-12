package com.around_team.todolist.ui.screens.schedule.models

import com.around_team.todolist.ui.common.models.Reminder
import com.around_team.todolist.ui.common.models.TodoItem

/**
 * Represents a scheduled item that can be either a Task or a Reminder
 */
sealed class ScheduledItem {
    abstract val time: Long
    abstract val isPast: Boolean

    data class TaskItem(
        val task: TodoItem,
        override val time: Long,
        override val isPast: Boolean
    ) : ScheduledItem()

    data class ReminderItem(
        val reminder: Reminder,
        override val time: Long,
        override val isPast: Boolean
    ) : ScheduledItem()
}


