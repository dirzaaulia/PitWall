package com.dirzaaulia.formula1.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthSizeClass {
    Compact,
    Medium,
    Expanded
}

data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val widthDp: Dp
) {
    val isCompact: Boolean get() = widthSizeClass == WindowWidthSizeClass.Compact
    val isMedium: Boolean get() = widthSizeClass == WindowWidthSizeClass.Medium
    val isExpanded: Boolean get() = widthSizeClass == WindowWidthSizeClass.Expanded
    val isWidescreen: Boolean get() = widthSizeClass == WindowWidthSizeClass.Expanded

    companion object {
        fun calculateFromWidth(widthDp: Dp): WindowSizeClass {
            val widthClass = when {
                widthDp < 600.dp -> WindowWidthSizeClass.Compact
                widthDp < 840.dp -> WindowWidthSizeClass.Medium
                else -> WindowWidthSizeClass.Expanded
            }
            return WindowSizeClass(widthClass, widthDp)
        }
    }
}

val LocalWindowSizeClass = compositionLocalOf {
    WindowSizeClass(WindowWidthSizeClass.Compact, 360.dp)
}
