package com.around_team.todolist.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.around_team.todolist.ui.common.models.Reminder

/**
 * Data class representing a Reminder entity in the local database.
 */
@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("task_id") val taskId: String? = null,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("trigger_time") val triggerTime: Long,
) {
    fun toReminder(): Reminder {
        return Reminder(
            id = id,
            taskId = taskId,
            title = title,
            description = description,
            triggerTime = triggerTime
        )
    }
}


