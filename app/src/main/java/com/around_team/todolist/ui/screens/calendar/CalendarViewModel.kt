package com.around_team.todolist.ui.screens.calendar

import androidx.lifecycle.viewModelScope
import com.around_team.todolist.data.db.DatabaseRepository
import com.around_team.todolist.data.db.ReminderRepository
import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.calendar.models.CalendarViewState
import com.around_team.todolist.ui.screens.schedule.models.ScheduledItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val reminderRepository: ReminderRepository,
) : BaseViewModel<CalendarViewState, Long>(initialState = CalendarViewState.createDefault()) {

    fun selectDate(dateMillis: Long) {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = dateMillis
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val startOfDay = calendar.timeInMillis
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                val endOfDay = calendar.timeInMillis

                val tasks = databaseRepository.getAllTodos()
                val reminders = reminderRepository.getRemindersByTimeRange(startOfDay, endOfDay)

                val items = mutableListOf<ScheduledItem>()

                tasks.filter { it.deadline != null && it.deadline!! >= startOfDay && it.deadline!! < endOfDay }
                    .forEach { task ->
                        items.add(
                            ScheduledItem.TaskItem(
                                task = task,
                                time = task.deadline!!,
                                isPast = false
                            )
                        )
                    }

                reminders.forEach { reminder ->
                    items.add(
                        ScheduledItem.ReminderItem(
                            reminder = reminder,
                            time = reminder.triggerTime,
                            isPast = false
                        )
                    )
                }

                viewState.update {
                    it.copy(
                        selectedDate = dateMillis,
                        itemsForSelectedDate = items.sortedBy { it.time }
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun setCurrentMonth(monthMillis: Long) {
        val currentSelectedDate = viewState.value.selectedDate
        viewState.update { it.copy(currentMonth = monthMillis) }
        // Refresh selected date when month changes
        currentSelectedDate?.let { selectDate(it) }
    }

    fun refreshSelectedDate() {
        val currentSelectedDate = viewState.value.selectedDate
        currentSelectedDate?.let { selectDate(it) }
    }
}

