package com.around_team.todolist.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.around_team.todolist.ui.screens.settings.models.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsEventBus {

    private val _currentSettings: MutableStateFlow<SettingsBundle?> = MutableStateFlow(null)
    val currentSettings: StateFlow<SettingsBundle?> = _currentSettings

    fun updateDarkMode(isDarkMode: Boolean) {
        _currentSettings.update {
            it?.copy(isDarkMode = isDarkMode, theme = null) 
                ?: SettingsBundle(isDarkMode = isDarkMode, theme = null)
        }
    }

    fun updateTheme(theme: Theme) {
        _currentSettings.update {
            it?.copy(theme = theme, isDarkMode = false) 
                ?: SettingsBundle(theme = theme, isDarkMode = false)
        }
    }
}

internal val LocalSettingsEventBus = staticCompositionLocalOf {
    SettingsEventBus()
}