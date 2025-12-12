package com.around_team.todolist.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.around_team.todolist.R
import com.around_team.todolist.ui.common.views.CustomIconButton
import com.around_team.todolist.ui.common.views.CustomTabRow
import com.around_team.todolist.ui.common.views.custom_toolbar.CustomToolbar
import com.around_team.todolist.ui.common.views.custom_toolbar.rememberToolbarScrollBehavior
import com.around_team.todolist.ui.screens.settings.models.SettingsEvent
import com.around_team.todolist.ui.screens.settings.models.Theme
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.around_team.todolist.utils.SetCompositionTheme


@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    toAboutScreen: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val viewState by viewModel.getViewState().collectAsStateWithLifecycle()

    SetCompositionTheme(viewState.selectedTheme)

    LaunchedEffect(viewState.shouldLogout) {
        if (viewState.shouldLogout) {
            onLogout()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomToolbar(
                navigationIcon = {
                    CustomIconButton(iconId = R.drawable.ic_back, onClick = onBackPressed)
                },
                collapsingTitle = stringResource(id = R.string.settings),
                expandedTitleStyle = JetTodoListTheme.typography.headline,
                collapsedTitleStyle = JetTodoListTheme.typography.headline,
                changeTitlePosition = false,
                scrollBehavior = rememberToolbarScrollBehavior(),
            )
        },
        containerColor = JetTodoListTheme.colors.back.primary,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileRow(
                userEmail = viewState.userEmail,
                userName = viewState.userName
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = JetTodoListTheme.colors.support.separator
            )
            ThemeRow(
                selected = viewState.selectedTheme,
                onChanged = { viewModel.obtainEvent(SettingsEvent.ThemeChanged(it)) },
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = JetTodoListTheme.colors.support.separator
            )
            AboutRow(toAboutScreen = toAboutScreen)
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = JetTodoListTheme.colors.support.separator
            )
            LogoutRow(
                onLogoutClick = { viewModel.obtainEvent(SettingsEvent.Logout) }
            )
        }
    }
}

@Composable
private fun ThemeRow(
    selected: Theme,
    onChanged: (Theme) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabList = Theme.entries

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = stringResource(id = R.string.theme),
            style = JetTodoListTheme.typography.body,
            color = JetTodoListTheme.colors.label.primary
        )
        CustomTabRow(
            modifier = Modifier.weight(1F),
            selectedTab = selected.ordinal,
            tabList = tabList,
            onTabChanged = { onChanged(Theme.getFromOrdinal(it)) },
            highlightSelectedTab = true
        ).Create()
    }
}

@Composable
private fun ProfileRow(
    userEmail: String?,
    userName: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.profile),
            style = JetTodoListTheme.typography.body,
            color = JetTodoListTheme.colors.label.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.logged_in_as),
            style = JetTodoListTheme.typography.subhead,
            color = JetTodoListTheme.colors.label.tertiary
        )
        userEmail?.let {
            Text(
                text = stringResource(id = R.string.user_email, it),
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.secondary
            )
        }
        userName?.let {
            Text(
                text = stringResource(id = R.string.user_name, it),
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.secondary
            )
        }
    }
}

@Composable
private fun AboutRow(
    modifier: Modifier = Modifier,
    toAboutScreen: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = toAboutScreen,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.about_app),
            style = JetTodoListTheme.typography.body,
            color = JetTodoListTheme.colors.label.primary
        )
    }
}

@Composable
private fun LogoutRow(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onLogoutClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.logout),
            style = JetTodoListTheme.typography.body,
            color = JetTodoListTheme.colors.colors.red
        )
    }
}
