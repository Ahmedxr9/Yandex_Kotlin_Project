package com.around_team.todolist.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.calendar.models.CalendarViewState
import com.around_team.todolist.ui.screens.schedule.models.ScheduledItem
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.FormatTimeInMillis
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
    val scrollBehavior = rememberToolbarScrollBehavior()

    // Refresh calendar data when screen becomes visible
    LaunchedEffect(Unit) {
        // Refresh selected date if one is selected
        viewState.selectedDate?.let { viewModel.selectDate(it) }
    }

    val calendar = Calendar.getInstance().apply {
        timeInMillis = viewState.currentMonth
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomToolbar(
                navigationIcon = {
                    com.around_team.todolist.ui.common.views.CustomIconButton(
                        iconId = R.drawable.ic_back,
                        onClick = onBackPressed
                    )
                },
                collapsingTitle = stringResource(id = R.string.calendar),
                expandedTitleStyle = JetTodoListTheme.typography.headline,
                collapsedTitleStyle = JetTodoListTheme.typography.headline,
                changeTitlePosition = false,
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = JetTodoListTheme.colors.back.primary,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month header
            Text(
                text = FormatTimeInMillis.format(viewState.currentMonth, "MMMM yyyy"),
                style = JetTodoListTheme.typography.title,
                color = JetTodoListTheme.colors.label.primary
            )

            // Calendar grid
            MonthCalendarView(
                year = year,
                month = month,
                selectedDate = viewState.selectedDate,
                onDateSelected = { dateMillis ->
                    viewModel.selectDate(dateMillis)
                }
            )

            // Selected date items
            if (viewState.selectedDate != null && viewState.itemsForSelectedDate.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = FormatTimeInMillis.format(viewState.selectedDate),
                    style = JetTodoListTheme.typography.headline,
                    color = JetTodoListTheme.colors.label.primary
                )

                viewState.itemsForSelectedDate.forEach { item ->
                    CalendarItemCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun MonthCalendarView(
    year: Int,
    month: Int,
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val today = Calendar.getInstance()
    val isCurrentMonth = today.get(Calendar.YEAR) == year && today.get(Calendar.MONTH) == month

    Column {
        // Weekday headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    style = JetTodoListTheme.typography.subhead,
                    color = JetTodoListTheme.colors.label.secondary,
                    modifier = Modifier.width(40.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar days
        var dayCounter = 1
        repeat(6) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayOfWeek ->
                    if (week == 0 && dayOfWeek < firstDayOfWeek - 1) {
                        // Empty cell before first day
                        Spacer(modifier = Modifier.width(40.dp))
                    } else if (dayCounter <= daysInMonth) {
                        val day = dayCounter++
                        val dateCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, day)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val dateMillis = dateCalendar.timeInMillis
                        val isSelected = selectedDate != null && dateMillis == selectedDate
                        val isToday = isCurrentMonth && day == today.get(Calendar.DAY_OF_MONTH)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected -> JetTodoListTheme.colors.colors.blue
                                        isToday -> JetTodoListTheme.colors.colors.blue.copy(alpha = 0.3f)
                                        else -> androidx.compose.ui.graphics.Color.Transparent
                                    }
                                )
                                .clickable { onDateSelected(dateMillis) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                style = JetTodoListTheme.typography.body,
                                color = when {
                                    isSelected -> JetTodoListTheme.colors.colors.white
                                    isToday -> JetTodoListTheme.colors.colors.blue
                                    else -> JetTodoListTheme.colors.label.primary
                                },
                                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarItemCard(
    item: ScheduledItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = JetTodoListTheme.colors.back.secondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                when (item) {
                    is ScheduledItem.TaskItem -> {
                        Text(
                            text = item.task.text,
                            style = JetTodoListTheme.typography.body,
                            color = JetTodoListTheme.colors.label.primary
                        )
                    }
                    is ScheduledItem.ReminderItem -> {
                        Text(
                            text = item.reminder.title,
                            style = JetTodoListTheme.typography.body,
                            color = JetTodoListTheme.colors.label.primary
                        )
                        Text(
                            text = item.reminder.description,
                            style = JetTodoListTheme.typography.subhead,
                            color = JetTodoListTheme.colors.label.secondary
                        )
                    }
                }
            }
            Text(
                text = FormatTimeInMillis.format(item.time, "HH:mm"),
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.secondary
            )
        }
    }
}

