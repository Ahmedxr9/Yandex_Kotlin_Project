package com.around_team.todolist.utils

import android.content.SharedPreferences
import com.around_team.todolist.ui.screens.settings.models.Theme
import java.util.UUID

/**
 * Helper class for managing preferences related to UUID.
 *
 * @property preferences The [SharedPreferences] instance used to store and retrieve preferences.
 */
class PreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        private const val KEY_UUID = "uuid"
        private const val KEY_TOKEN = "KEY_TOKEN"
        private const val KEY_THEME = "KEY_THEME"
        private const val KEY_USER_EMAIL = "KEY_USER_EMAIL"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
    }

    fun saveSelectedTheme(theme: Theme) {
        preferences.edit()
            .putString(KEY_THEME, theme.name)
            .apply()
    }

    fun getSelectedTheme(): Theme? {
        val strTheme = preferences.getString(KEY_THEME, null)
        return strTheme?.let { Theme.valueOf(it) }
    }

    fun getUUID(): String {
        var uuid = preferences.getString(KEY_UUID, null)
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            preferences.edit()
                .putString(KEY_UUID, uuid)
                .apply()
        }
        return uuid
    }

    fun saveToken(token: String) {
        preferences.edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        preferences.edit()
            .remove(KEY_TOKEN)
            .apply()
    }

    fun saveUserEmail(email: String) {
        preferences.edit()
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    fun getUserEmail(): String? {
        return preferences.getString(KEY_USER_EMAIL, null)
    }

    fun saveUserName(name: String) {
        preferences.edit()
            .putString(KEY_USER_NAME, name)
            .apply()
    }

    fun getUserName(): String? {
        return preferences.getString(KEY_USER_NAME, null)
    }

    fun clearUserData() {
        preferences.edit()
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .apply()
    }
}
