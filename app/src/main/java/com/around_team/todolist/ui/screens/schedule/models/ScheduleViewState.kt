package com.around_team.todolist.ui.screens.schedule.models

data class ScheduleViewState(
    val scheduledItems: List<ScheduledItem> = emptyList(),
    val groupedItems: Map<String, List<ScheduledItem>> = emptyMap(),
    val isLoading: Boolean = false,
)


