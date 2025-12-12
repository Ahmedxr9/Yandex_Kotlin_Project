package com.around_team.todolist.ui.screens.registration.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.around_team.todolist.R
import com.around_team.todolist.ui.theme.JetTodoListTheme

/**
 * A composable function that creates a button for signing in with Google.
 *
 * @param onClick A lambda function to be executed when the button is clicked.
 * @param modifier A modifier to be applied to the button, defaulting to an empty modifier.
 * @param enabled Whether the button is enabled, defaulting to true.
 */
@Composable
fun SignInWithGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = androidx.compose.ui.graphics.Color(0.0F, 0.0F, 0.0F, 0.1F)),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = JetTodoListTheme.colors.colors.white,
            contentColor = JetTodoListTheme.colors.label.primary,
            disabledContainerColor = JetTodoListTheme.colors.back.secondary,
            disabledContentColor = JetTodoListTheme.colors.label.disable
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Google "G" icon representation
            Text(
                text = "G",
                style = JetTodoListTheme.typography.headline,
                fontWeight = FontWeight.Bold,
                color = if (enabled) {
                    androidx.compose.ui.graphics.Color(0xFF4285F4)
                } else {
                    JetTodoListTheme.colors.label.disable
                }
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.sign_in_with_google),
                color = if (enabled) {
                    JetTodoListTheme.colors.label.primary
                } else {
                    JetTodoListTheme.colors.label.disable
                },
                style = JetTodoListTheme.typography.headline
            )
        }
    }
}

@Preview
@Composable
private fun SignInWithGoogleButtonPreview() {
    SignInWithGoogleButton({})
}

