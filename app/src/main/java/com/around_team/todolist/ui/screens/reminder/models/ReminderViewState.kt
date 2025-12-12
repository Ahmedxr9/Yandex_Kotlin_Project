package com.around_team.todolist.ui.screens.reminder.models

import com.around_team.todolist.ui.common.models.Reminder
import com.around_team.todolist.ui.common.models.TodoItem

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList(),
    val availableTasks: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)


