package com.around_team.todolist.ui.screens.registration

import com.around_team.todolist.ui.common.models.BaseViewModel
import com.around_team.todolist.ui.screens.registration.models.RegistrationEvent
import com.around_team.todolist.ui.screens.registration.models.RegistrationViewState
import com.around_team.todolist.utils.PreferencesHelper
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * A ViewModel for managing the state and events of the registration screen.
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : BaseViewModel<RegistrationViewState, RegistrationEvent>(initialState = RegistrationViewState()) {

    override fun obtainEvent(viewEvent: RegistrationEvent) {
        when (viewEvent) {
            is RegistrationEvent.HandleResult -> handleResult(viewEvent.result)
            is RegistrationEvent.HandleGoogleSignInResult -> handleGoogleSignInResult(viewEvent.account)
            is RegistrationEvent.HandleGoogleSignInError -> handleGoogleSignInError(viewEvent.message)
        }
    }

    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessAuth(result.token)
            is YandexAuthResult.Failure -> onProcessError(result.exception)
            YandexAuthResult.Cancelled -> {}
        }
    }

    private fun onSuccessAuth(token: YandexAuthToken) {
        val tokenValue = token.value
        preferencesHelper.saveToken(tokenValue)
        viewState.update { it.copy(toNextScreen = true) }
    }

    private fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
        if (account != null) {
            // NOTE: Using Android Client ID - ID tokens are NOT available
            // Android Client ID only provides: email, account ID, display name, photo URL
            // We use email or account ID to create a local authentication token
            // This is "basic Google login" - no server-side token verification possible
            
            val email = account.email
            val displayName = account.displayName
            val accountId = account.id

            // Create authentication token using email (preferred) or account ID
            // Format: "google_{email}" or "google_{accountId}"
            val token = if (email != null && email.isNotBlank()) {
                "google_${email}"
            } else if (accountId != null) {
                "google_${accountId}"
            } else {
                // Fallback: use account ID from GoogleSignInAccount
                "google_${account.id}"
            }
            
            preferencesHelper.saveToken(token)
            
            // Save user information for profile display
            if (email != null) preferencesHelper.saveUserEmail(email)
            if (displayName != null) preferencesHelper.saveUserName(displayName)
            
            viewState.update { it.copy(toNextScreen = true) }
        } else {
            viewState.update {
                it.copy(message = "Google Sign-In failed: Account is null")
            }
        }
    }

    private fun handleGoogleSignInError(message: String) {
        viewState.update { it.copy(message = message) }
    }

    private fun onProcessError(exception: Exception) {
        viewState.update { it.copy(message = exception.message) }
    }
}