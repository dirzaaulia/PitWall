package com.dirzaaulia.formula1.ui.screen.app_info

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dirzaaulia.formula1.BuildConfig
import com.dirzaaulia.formula1.R

@Composable
fun AppInfo() {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("All data in this app is provided by ")

        pushStringAnnotation(tag = "URL", annotation = "https://f1api.dev")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append("f1api.dev")
        }
        pop()
    }

    val annotatedString2 = buildAnnotatedString {
        append("Checkout my personal ")

        pushStringAnnotation(tag = "URL", annotation = "https://dirzaaulia.com")
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append("Website")
        }
        pop()
    }

    val annotatedString3 = buildAnnotatedString {
        append("Checkout my another app in ")

        pushStringAnnotation(tag = "URL", annotation = "https://play.google.com/store/apps/dev?id=4806849608818858118")

        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            )
        ) {
            append("Google Play Store")
        }
        pop()
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(R.string.app_fullname),
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "Version ${BuildConfig.VERSION_CODE}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "By Dirza Aulia",
                        style = MaterialTheme.typography.titleMedium
                    )
                    ShapedHorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 4.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(50)
                    )
                    Text(
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, "https://dirzaaulia.com".toUri())
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        text = annotatedString2,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/dev?id=4806849608818858118".toUri())
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        text = annotatedString3,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, "https://f1api.dev".toUri())
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        text = annotatedString,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
        }
    }
}

@Composable
fun ShapedHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp, // Default thickness for HorizontalDivider
    color: Color = Color.Gray, // Default color
    shape: androidx.compose.ui.graphics.Shape // Parameter to pass the shape
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness) // The height of the Box is the thickness of the divider
            .clip(shape)       // Apply the clipping shape HERE
            .background(color)
    )
}