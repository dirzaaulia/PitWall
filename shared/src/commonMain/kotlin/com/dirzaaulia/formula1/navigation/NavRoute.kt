package com.dirzaaulia.formula1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector
import com.dirzaaulia.formula1.model.Race

enum class NavTab(val title: String, val icon: ImageVector) {
    HOME("HOME", Icons.Default.Home),
    TELEMETRY("TELEMETRY", Icons.Default.Speed),
    CALENDAR("CALENDAR", Icons.Default.CalendarMonth),
    STANDINGS("STANDINGS", Icons.Default.Leaderboard),
    INFO("INFO", Icons.Default.Info)
}

sealed interface NavRoute {
    data object Home : NavRoute
    data object Telemetry : NavRoute
    data object Calendar : NavRoute
    data object Standings : NavRoute
    data object Info : NavRoute
    data class RaceDetail(val race: Race) : NavRoute
}
