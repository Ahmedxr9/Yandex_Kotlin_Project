package com.around_team.todolist.ui.screens.schedule

import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.db.ReminderRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.schedule.models.ScheduledItem
import com.around_team.todolist.ui.screens.schedule.models.ScheduleViewState
import com.around_team.todolist.utils.FormatTimeInMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val reminderRepository: ReminderRepository,
) : BaseViewModel<ScheduleViewState, Unit>(initialState = ScheduleViewState()) {

    init {
        loadSchedule()
    }

    fun loadSchedule() {
        viewModelScope.launch {
            try {
                viewState.update { it.copy(isLoading = true) }
                
                val tasks = databaseRepository.getAllTodos()
                val reminders = reminderRepository.getAllReminders()
                val currentTime = System.currentTimeMillis()

                val scheduledItems = mutableListOf<ScheduledItem>()

                // Add tasks with deadlines
                tasks.filter { it.deadline != null }.forEach { task ->
                    scheduledItems.add(
                        ScheduledItem.TaskItem(
                            task = task,
                            time = task.deadline!!,
                            isPast = task.deadline!! < currentTime
                        )
                    )
                }

                // Add reminders
                reminders.forEach { reminder ->
                    scheduledItems.add(
                        ScheduledItem.ReminderItem(
                            reminder = reminder,
                            time = reminder.triggerTime,
                            isPast = reminder.triggerTime < currentTime
                        )
                    )
                }

                // Sort by time
                val sortedItems = scheduledItems.sortedBy { it.time }

                // Group by day
                val groupedItems = sortedItems.groupBy { item ->
                    FormatTimeInMillis.format(item.time, "yyyy-MM-dd")
                }

                viewState.update {
                    it.copy(
                        scheduledItems = sortedItems,
                        groupedItems = groupedItems,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                viewState.update { it.copy(isLoading = false) }
            }
        }
    }
}

