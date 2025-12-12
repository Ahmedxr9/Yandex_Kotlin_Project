package com.around_team.todolist.ui.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.schedule.models.ScheduledItem
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.FormatTimeInMillis
import java.util.Calendar

@Composable
fun ScheduleScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
    val scrollBehavior = rememberToolbarScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.loadSchedule()
    }

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
                collapsingTitle = stringResource(id = R.string.schedule),
                expandedTitleStyle = JetTodoListTheme.typography.headline,
                collapsedTitleStyle = JetTodoListTheme.typography.headline,
                changeTitlePosition = false,
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = JetTodoListTheme.colors.back.primary,
    ) { paddingValues ->
        if (viewState.isLoading) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                viewState.groupedItems.forEach { (dateKey, items) ->
                    val dateMillis = items.firstOrNull()?.time ?: 0L
                    val isToday = dateMillis >= today && dateMillis < today + 86400000
                    val isPast = items.firstOrNull()?.isPast == true

                    Text(
                        text = FormatTimeInMillis.format(dateMillis),
                        style = JetTodoListTheme.typography.title,
                        color = when {
                            isToday -> JetTodoListTheme.colors.colors.blue
                            isPast -> JetTodoListTheme.colors.label.tertiary
                            else -> JetTodoListTheme.colors.label.primary
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    items.forEach { item ->
                        ScheduledItemCard(
                            item = item,
                            isPast = isPast
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (viewState.groupedItems.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_scheduled_items),
                        style = JetTodoListTheme.typography.body,
                        color = JetTodoListTheme.colors.label.secondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduledItemCard(
    item: ScheduledItem,
    isPast: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isPast) {
                JetTodoListTheme.colors.back.secondary.copy(alpha = 0.5f)
            } else {
                JetTodoListTheme.colors.back.secondary
            }
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
                            color = if (isPast) {
                                JetTodoListTheme.colors.label.tertiary
                            } else {
                                JetTodoListTheme.colors.label.primary
                            }
                        )
                    }
                    is ScheduledItem.ReminderItem -> {
                        Text(
                            text = item.reminder.title,
                            style = JetTodoListTheme.typography.body,
                            color = if (isPast) {
                                JetTodoListTheme.colors.label.tertiary
                            } else {
                                JetTodoListTheme.colors.label.primary
                            }
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


