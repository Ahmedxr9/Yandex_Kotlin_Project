package com.around_team.todolist.ui.screens.settings.models


data class SettingsViewState(
    val selectedTheme: Theme = Theme.Auto,
    val userEmail: String? = null,
    val userName: String? = null,
    val shouldLogout: Boolean = false
)