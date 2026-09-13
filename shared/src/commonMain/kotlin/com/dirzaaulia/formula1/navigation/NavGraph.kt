package com.dirzaaulia.formula1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dirzaaulia.formula1.model.Driver
import com.dirzaaulia.formula1.model.Team
import com.dirzaaulia.formula1.ui.screen.app_info.AppInfoScreen
import com.dirzaaulia.formula1.ui.screen.calendar.CalendarScreen
import com.dirzaaulia.formula1.ui.screen.home.HomeScreen
import com.dirzaaulia.formula1.ui.screen.race_detail.RaceDetailScreen
import com.dirzaaulia.formula1.ui.screen.standings.StandingsScreen
import com.dirzaaulia.formula1.ui.screen.telemetry.TelemetryScreen

@Composable
fun NavGraph(
    currentRoute: NavRoute,
    selectedSeason: Int,
    onSeasonChanged: (Int) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentRoute) {
        is NavRoute.Home -> HomeScreen(
            selectedSeason = selectedSeason,
            onNavigateToTelemetry = { onNavigate(NavRoute.Telemetry) },
            onNavigateToStandings = { onNavigate(NavRoute.Standings) },
            onNavigateToCalendar = { onNavigate(NavRoute.Calendar) },
            onSelectRace = { race -> onNavigate(NavRoute.RaceDetail(race)) }
        )
        is NavRoute.Telemetry -> TelemetryScreen(
            selectedSeason = selectedSeason
        )
        is NavRoute.Calendar -> CalendarScreen(
            selectedSeason = selectedSeason,
            onSeasonChanged = onSeasonChanged,
            onSelectRace = { race -> onNavigate(NavRoute.RaceDetail(race)) }
        )
        is NavRoute.Standings -> StandingsScreen(
            selectedSeason = selectedSeason
        )
        is NavRoute.Info -> AppInfoScreen()
        is NavRoute.RaceDetail -> RaceDetailScreen(
            race = currentRoute.race,
            onBack = onBack
        )
    }
}
