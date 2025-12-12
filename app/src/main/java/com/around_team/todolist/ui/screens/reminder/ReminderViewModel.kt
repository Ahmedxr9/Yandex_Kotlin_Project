package com.around_team.todolist.ui.screens.reminder

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.db.ReminderRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.reminder.models.ReminderEvent
import com.around_team.todolist.ui.screens.reminder.models.ReminderViewState
import com.around_team.todolist.utils.ReminderNotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val databaseRepository: DatabaseRepository,
) : BaseViewModel<ReminderViewState, ReminderEvent>(initialState = ReminderViewState()) {

    init {
        loadReminders()
        loadTasks()
    }

    override fun obtainEvent(viewEvent: ReminderEvent) {
        when (viewEvent) {
            is ReminderEvent.SaveReminder -> saveReminder(viewEvent)
            is ReminderEvent.DeleteReminder -> deleteReminder(viewEvent.id)
            ReminderEvent.LoadReminders -> loadReminders()
        }
    }

    fun saveReminderWithContext(event: ReminderEvent.SaveReminder, context: Context) {
        viewModelScope.launch {
            try {
                viewState.update { it.copy(isLoading = true, errorMessage = null) }
                val reminder = com.around_team.todolist.ui.common.models.Reminder(
                    title = event.title,
                    description = event.description,
                    triggerTime = event.triggerTime,
                    taskId = event.taskId
                )
                val reminderId = reminderRepository.insertReminder(reminder)
                
                // Schedule notification with the actual reminder ID
                ReminderNotificationHelper.scheduleReminder(context, reminderId, event.triggerTime)
                
                loadReminders()
            } catch (e: Exception) {
                viewState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save reminder: ${e.message}"
                    )
                }
            }
        }
    }

    private fun saveReminder(event: ReminderEvent.SaveReminder) {
        // This method is kept for compatibility but won't schedule notifications
        // Use saveReminderWithContext instead
        viewModelScope.launch {
            try {
                viewState.update { it.copy(isLoading = true, errorMessage = null) }
                val reminder = com.around_team.todolist.ui.common.models.Reminder(
                    title = event.title,
                    description = event.description,
                    triggerTime = event.triggerTime,
                    taskId = event.taskId
                )
                reminderRepository.insertReminder(reminder)
                loadReminders()
            } catch (e: Exception) {
                viewState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save reminder: ${e.message}"
                    )
                }
            }
        }
    }

    private fun deleteReminder(id: Long) {
        viewModelScope.launch {
            try {
                reminderRepository.deleteReminderById(id)
                loadReminders()
            } catch (e: Exception) {
                viewState.update {
                    it.copy(errorMessage = "Failed to delete reminder: ${e.message}")
                }
            }
        }
    }

    private fun loadReminders() {
        viewModelScope.launch {
            try {
                val reminders = reminderRepository.getAllReminders()
                viewState.update {
                    it.copy(reminders = reminders, isLoading = false, errorMessage = null)
                }
            } catch (e: Exception) {
                viewState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load reminders: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            try {
                val tasks = databaseRepository.getAllTodos()
                viewState.update { it.copy(availableTasks = tasks) }
            } catch (e: Exception) {
                // Silently fail - tasks are optional
            }
        }
    }
}

