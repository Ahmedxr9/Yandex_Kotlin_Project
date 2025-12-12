package com.around_team.todolist.ui.screens.about

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.around_team.todolist.R
import com.around_team.todolist.data.network.TodoListConfig
import com.around_team.todolist.ui.theme.JetTodoListTheme

@Composable
fun AboutAppScreen(onBackPressed: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Get app version
    val versionName = try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: TodoListConfig.DEFAULT_VERSION_NAME
    } catch (e: PackageManager.NameNotFoundException) {
        TodoListConfig.DEFAULT_VERSION_NAME
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(JetTodoListTheme.colors.back.primary)
            .padding(WindowInsets.systemBars.only(WindowInsetsSides.Top).asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Top bar with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = JetTodoListTheme.colors.label.primary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.about_the_application),
                    style = JetTodoListTheme.typography.body,
                    fontWeight = FontWeight.Medium,
                    color = JetTodoListTheme.colors.label.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp)) // Balance the back button
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Author image
                AuthorImage(
                    imageUrl = stringResource(R.string.author_image_url),
                    modifier = Modifier.size(140.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Author name
                Text(
                    text = stringResource(R.string.author_name),
                    style = JetTodoListTheme.typography.body,
                    color = JetTodoListTheme.colors.label.primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Author label
                Text(
                    text = stringResource(R.string.author_label),
                    style = JetTodoListTheme.typography.subhead,
                    color = JetTodoListTheme.colors.label.tertiary
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Description card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = JetTodoListTheme.colors.back.secondary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_description),
                        style = JetTodoListTheme.typography.body,
                        color = JetTodoListTheme.colors.label.primary,
                        lineHeight = JetTodoListTheme.typography.body.lineHeight
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Social Links section
                Text(
                    text = stringResource(R.string.social_links),
                    style = JetTodoListTheme.typography.body,
                    fontWeight = FontWeight.Medium,
                    color = JetTodoListTheme.colors.label.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Social buttons row
                val githubUrl = stringResource(R.string.github_url)
                val twitterUrl = stringResource(R.string.twitter_url)
                val telegramUrl = stringResource(R.string.telegram_url)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialButton(
                        iconRes = R.drawable.ic_github,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
                            context.startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    SocialButton(
                        iconRes = R.drawable.ic_x_twitter,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl))
                            context.startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    SocialButton(
                        iconRes = R.drawable.ic_telegram,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl))
                            context.startActivity(intent)
                        }
                    )
                }
            }

            // Version at bottom
            Text(
                text = stringResource(R.string.version_label, versionName),
                style = JetTodoListTheme.typography.subhead,
                color = JetTodoListTheme.colors.label.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AuthorImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.author_image),
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun SocialButton(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(JetTodoListTheme.colors.back.secondary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(JetTodoListTheme.colors.label.primary)
        )
    }
}
