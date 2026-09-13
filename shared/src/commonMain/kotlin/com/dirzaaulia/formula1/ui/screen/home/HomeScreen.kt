package com.dirzaaulia.formula1.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dirzaaulia.formula1.model.ConstructorsStandings
import com.dirzaaulia.formula1.model.Driver
import com.dirzaaulia.formula1.model.DriverStandings
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.network.NetworkRepository
import com.dirzaaulia.formula1.ui.component.ShimmerCard
import com.dirzaaulia.formula1.util.LocalWindowSizeClass
import com.dirzaaulia.formula1.util.getCurrentDateIso

@Composable
fun HomeScreen(
    selectedSeason: Int,
    onNavigateToTelemetry: () -> Unit,
    onNavigateToStandings: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onSelectRace: (Race) -> Unit
) {
    var isLoading by remember(selectedSeason) { mutableStateOf(true) }
    var races by remember(selectedSeason) { mutableStateOf<List<Race>>(emptyList()) }
    var driverStandings by remember(selectedSeason) { mutableStateOf<List<DriverStandings>>(emptyList()) }
    var constructorStandings by remember(selectedSeason) { mutableStateOf<List<ConstructorsStandings>>(emptyList()) }
    var selectedHeroDriver by remember { mutableStateOf<DriverStandings?>(null) }

    LaunchedEffect(selectedSeason) {
        isLoading = true
        races = NetworkRepository.getRaces(selectedSeason)
        val fetchedDrivers = NetworkRepository.getDriverStandings(selectedSeason)
        val fetchedConstructors = NetworkRepository.getConstructorStandings(selectedSeason)
        driverStandings = fetchedDrivers
        constructorStandings = fetchedConstructors
        if (selectedHeroDriver == null || !fetchedDrivers.any { it.driver.driverId == selectedHeroDriver?.driver?.driverId }) {
            selectedHeroDriver = fetchedDrivers.firstOrNull()
        }
        isLoading = false
    }

    val today = getCurrentDateIso()
    // Next race is strictly from schedule — independent of selected driver
    val nextRace = races.firstOrNull { it.date >= today } ?: races.lastOrNull() ?: Race()
    val topDriverStandings = driverStandings.take(3)
    val topConstructors = constructorStandings.take(3)

    val fallbackLeader = remember {
        DriverStandings(
            driver = Driver(
                driverId = "max_verstappen",
                name = "Max",
                surname = "Verstappen",
                nationality = "Dutch",
                number = 1,
                code = "VER",
                team = "Red Bull Racing",
                points = 295.0,
                wins = 7
            ),
            position = 1,
            points = 295.0,
            wins = 7
        )
    }
    val activeHeroStanding = selectedHeroDriver ?: driverStandings.firstOrNull() ?: fallbackLeader

    // Fixed viewport — no scroll, fills screen height
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            ShimmerCard(height = 800.dp)
        } else {
            EditorialHomeDashboard(
                nextRace = nextRace,
                topDriverStandings = topDriverStandings,
                allDriverStandings = driverStandings,
                topConstructors = topConstructors,
                heroStanding = activeHeroStanding,
                onSelectDriver = { selectedHeroDriver = it },
                onNavigateToTelemetry = onNavigateToTelemetry,
                onNavigateToStandings = onNavigateToStandings,
                onNavigateToCalendar = onNavigateToCalendar,
                onSelectRace = onSelectRace
            )
        }
    }
}

private val Int.dp get() = androidx.compose.ui.unit.Dp(this.toFloat())
