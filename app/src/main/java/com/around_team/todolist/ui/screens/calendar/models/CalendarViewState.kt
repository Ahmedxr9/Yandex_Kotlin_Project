package com.around_team.todolist.ui.screens.calendar.models

import com.around_team.todolist.ui.screens.schedule.models.ScheduledItem

data class CalendarViewState(
    val selectedDate: Long? = null,
    val itemsForSelectedDate: List<ScheduledItem> = emptyList(),
    val currentMonth: Long = 0L,
) {
    companion object {
        fun createDefault(): CalendarViewState {
            return CalendarViewState(currentMonth = System.currentTimeMillis())
        }
    }
}

