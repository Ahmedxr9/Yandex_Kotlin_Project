package com.around_team.todolist.ui.screens.registration

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// Note: TodoListConfig.GOOGLE_CLIENT_ID is not used in GoogleSignInOptions
// because Android Client ID cannot be used with requestIdToken()
// The Client ID is configured in Google Cloud Console for the Android app package name
import com.around_team.todolist.ui.screens.registration.models.RegistrationEvent
import com.around_team.todolist.ui.screens.registration.views.SignInWithGoogleButton
import com.around_team.todolist.ui.screens.registration.views.SignInWithYandexIdButton
import com.around_team.todolist.ui.theme.JetTodoListTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.GoogleApiAvailability
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

/**
 * A screen for handling user registration.
 *
 * @property viewModel The ViewModel instance that handles the business logic and state for the registration screen.
 * @property toNextScreen A lambda function to navigate to the next screen after successful registration.
 */

@Composable
fun RegistrationScreen(
    toNextScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val viewState by viewModel.getViewState().collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Check Google Play Services availability
    val googleApiAvailability = remember { GoogleApiAvailability.getInstance() }
    val isGooglePlayServicesAvailable = remember {
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS
    }
    
    // Yandex Auth setup
    val sdk = YandexAuthSdk.create(YandexAuthOptions(context))
    val yandexLauncher = rememberLauncherForActivityResult(sdk.contract) { result ->
        viewModel.obtainEvent(RegistrationEvent.HandleResult(result))
    }
    val yandexOptions = YandexAuthLoginOptions()
    
    // Google Sign-In setup
    // NOTE: Using Android Client ID for basic Google Sign-In (not Web Client ID)
    // Android Client IDs cannot issue ID tokens - they only provide:
    // - Email address
    // - Google account ID
    // - Display name
    // - Photo URL
    // This is a "basic Google login" setup that does NOT support server-side token verification.
    // For server-side verification, you would need a Web Client ID and requestIdToken().
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            // Intentionally NOT using .requestIdToken() because:
            // 1. Android Client ID cannot issue ID tokens
            // 2. ID tokens require Web Client ID (OAuth 2.0 Client ID)
            // 3. This setup uses basic Google authentication only
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }
    
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data == null) {
            viewModel.obtainEvent(
                RegistrationEvent.HandleGoogleSignInError(
                    "Google Sign-In cancelled or failed: No data returned"
                )
            )
            return@rememberLauncherForActivityResult
        }
        
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            viewModel.obtainEvent(RegistrationEvent.HandleGoogleSignInResult(account))
        } catch (e: ApiException) {
            val errorMessage = when (e.statusCode) {
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> 
                    "Google Sign-In was cancelled"
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS -> 
                    "Google Sign-In is already in progress"
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_FAILED -> 
                    "Google Sign-In failed. Please try again"
                else -> "Google Sign-In failed with error code: ${e.statusCode}"
            }
            viewModel.obtainEvent(
                RegistrationEvent.HandleGoogleSignInError(errorMessage)
            )
        } catch (e: Exception) {
            viewModel.obtainEvent(
                RegistrationEvent.HandleGoogleSignInError(
                    "Google Sign-In error: ${e.message ?: "Unknown error"}"
                )
            )
        }
    }

    if (viewState.message != null) {
        LaunchedEffect(viewState.message) {
            Toast.makeText(context, viewState.message, Toast.LENGTH_SHORT).show()
        }
    }

    if (viewState.toNextScreen) toNextScreen()

    Surface(modifier = modifier.fillMaxSize(), color = JetTodoListTheme.colors.back.primary) {
        Box(
            modifier = Modifier.padding(horizontal = 50.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                SignInWithGoogleButton(
                    onClick = {
                        if (!isGooglePlayServicesAvailable) {
                            viewModel.obtainEvent(
                                RegistrationEvent.HandleGoogleSignInError(
                                    "Google Play Services is not available. Please install Google Play Services from the Play Store."
                                )
                            )
                        } else {
                            try {
                                googleLauncher.launch(googleSignInClient.signInIntent)
                            } catch (e: Exception) {
                                viewModel.obtainEvent(
                                    RegistrationEvent.HandleGoogleSignInError(
                                        "Failed to start Google Sign-In: ${e.message ?: "Unknown error"}"
                                    )
                                )
                            }
                        }
                    },
                    enabled = !viewState.toNextScreen && isGooglePlayServicesAvailable
                )
                Spacer(modifier = Modifier.height(16.dp))
                SignInWithYandexIdButton(onClick = { yandexLauncher.launch(yandexOptions) })
            }
        }
    }
}
