package com.around_team.todolist.ui.common.models

import com.around_team.todolist.data.db.entities.ReminderEntity

/**
 * Data class representing a Reminder.
 */
data class Reminder(
    val id: Long = 0,
    val taskId: String? = null,
    val title: String,
    val description: String,
    val triggerTime: Long,
) {
    fun toEntity(): ReminderEntity {
        return ReminderEntity(
            id = id,
            taskId = taskId,
            title = title,
            description = description,
            triggerTime = triggerTime
        )
    }
}


