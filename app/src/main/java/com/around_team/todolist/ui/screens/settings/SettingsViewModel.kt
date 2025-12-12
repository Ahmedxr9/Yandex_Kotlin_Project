package com.around_team.todolist.ui.screens.settings

import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.settings.models.SettingsEvent
import com.around_team.todolist.ui.screens.settings.models.SettingsViewState
import com.around_team.todolist.ui.screens.settings.models.Theme
import com.around_team.todolist.utils.PreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : BaseViewModel<SettingsViewState, SettingsEvent>(initialState = SettingsViewState()) {
    private var selectedTheme: Theme = Theme.Auto

    init {
        selectedTheme = preferencesHelper.getSelectedTheme() ?: Theme.Auto
        val userEmail = preferencesHelper.getUserEmail()
        val userName = preferencesHelper.getUserName()
        viewState.update {
            it.copy(
                selectedTheme = selectedTheme,
                userEmail = userEmail,
                userName = userName
            )
        }
    }

    override fun obtainEvent(viewEvent: SettingsEvent) {
        when (viewEvent) {
            is SettingsEvent.ThemeChanged -> changeTheme(viewEvent.theme)
            is SettingsEvent.Logout -> handleLogout()
        }
    }

    private fun changeTheme(theme: Theme) {
        preferencesHelper.saveSelectedTheme(theme)
        viewState.update { it.copy(selectedTheme = theme) }
    }

    private fun handleLogout() {
        preferencesHelper.clearToken()
        preferencesHelper.clearUserData()
        viewState.update {
            it.copy(
                shouldLogout = true,
                userEmail = null,
                userName = null
            )
        }
    }
}