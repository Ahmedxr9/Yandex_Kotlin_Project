package com.around_team.todolist.ui.theme

import com.around_team.todolist.ui.screens.settings.models.Theme

data class SettingsBundle(
    val isDarkMode: Boolean = false,
    val theme: Theme? = null,
)