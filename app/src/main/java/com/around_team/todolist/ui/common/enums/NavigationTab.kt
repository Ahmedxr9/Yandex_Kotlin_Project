package com.around_team.todolist.ui.common.enums

import com.around_team.todolist.R

/**
 * Enum representing navigation tabs for the main screen.
 */
enum class NavigationTab(override val text: Int? = null, override val iconId: Int? = null) : ITabs {
    AllTasks(text = R.string.all_tasks, iconId = R.drawable.ic_list),
    Reminder(text = R.string.reminder, iconId = R.drawable.ic_reminder),
    Schedule(text = R.string.schedule, iconId = R.drawable.ic_schedule),
    Calendar(text = R.string.calendar, iconId = R.drawable.ic_calendar);

    companion object {
        fun getFromOrdinal(ordinal: Int): NavigationTab {
            return when (ordinal) {
                0 -> AllTasks
                1 -> Reminder
                2 -> Schedule
                3 -> Calendar
                else -> throw IllegalArgumentException(ordinal.toString())
            }
        }
    }
}


