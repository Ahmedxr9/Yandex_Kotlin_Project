package com.around_team.todolist.ui.screens.registration.models

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.yandex.authsdk.YandexAuthResult

/**
 * A sealed class representing different events that can occur during the registration process.
 */
sealed class RegistrationEvent {
    data class HandleResult(val result: YandexAuthResult): RegistrationEvent()
    data class HandleGoogleSignInResult(val account: GoogleSignInAccount?): RegistrationEvent()
    data class HandleGoogleSignInError(val message: String): RegistrationEvent()
}