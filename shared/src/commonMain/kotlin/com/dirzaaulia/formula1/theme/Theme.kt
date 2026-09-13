package com.dirzaaulia.formula1.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun FormulaTrackrTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground,
            surface = DarkSurface,
            surfaceVariant = DarkCard,
            primary = AccentRed,
            onPrimary = TextPrimary,
            onBackground = TextPrimary,
            onSurface = TextPrimary,
            onSurfaceVariant = TextSecondary,
            outline = DarkBorder,
            outlineVariant = DarkBorderSubtle
        ),
        content = content
    )
}
