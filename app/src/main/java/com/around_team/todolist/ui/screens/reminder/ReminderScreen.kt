package com.around_team.todolist.ui.screens.reminder

import android.app.TimePickerDialog
import android.content.Context
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.CustomButton
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.edit.views.CustomDatePicker
import com.around_team.todolist.ui.screens.reminder.models.ReminderEvent
import com.around_team.todolist.ui.theme.JetTodoListTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollBehavior = rememberToolbarScrollBehavior()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var selectedTaskId by remember { mutableStateOf<String?>(null) }

    val datePickerState = androidx.compose.material3.rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

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
                collapsingTitle = stringResource(id = R.string.reminder),
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
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.reminder_title)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = JetTodoListTheme.colors.back.secondary,
                    unfocusedContainerColor = JetTodoListTheme.colors.back.secondary,
                )
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = JetTodoListTheme.colors.back.secondary,
                    unfocusedContainerColor = JetTodoListTheme.colors.back.secondary,
                )
            )

            // Date picker
            CustomDatePicker(state = datePickerState)

            // Time picker button
            CustomButton(
                text = selectedTime?.let { "${it.first}:${it.second.toString().padStart(2, '0')}" }
                    ?: stringResource(R.string.select_time),
                onClick = {
                    val calendar = Calendar.getInstance()
                    selectedDate?.let {
                        calendar.timeInMillis = it
                    }
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            selectedTime = Pair(hourOfDay, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }
            )

            // Task selection (optional)
            if (viewState.availableTasks.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.attach_to_task),
                    style = JetTodoListTheme.typography.body,
                    color = JetTodoListTheme.colors.label.secondary
                )
                // Simple dropdown or list for task selection
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = stringResource(R.string.save_reminder),
                onClick = {
                    val date = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    val time = selectedTime ?: Pair(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE))
                    
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = date
                        set(Calendar.HOUR_OF_DAY, time.first)
                        set(Calendar.MINUTE, time.second)
                        set(Calendar.SECOND, 0)
                    }
                    
                    val triggerTime = calendar.timeInMillis
                    
                    viewModel.saveReminderWithContext(
                        ReminderEvent.SaveReminder(
                            title = title,
                            description = description,
                            triggerTime = triggerTime,
                            taskId = selectedTaskId
                        ),
                        context
                    )
                    
                    // Reset form
                    title = ""
                    description = ""
                    selectedDate = null
                    selectedTime = null
                    selectedTaskId = null
                    
                    // Navigate back after saving
                    onBackPressed()
                }
            )
        }
    }
}

