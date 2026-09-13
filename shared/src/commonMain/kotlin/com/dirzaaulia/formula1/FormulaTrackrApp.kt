package com.dirzaaulia.formula1

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.Driver
import com.dirzaaulia.formula1.model.Team
import com.dirzaaulia.formula1.navigation.NavGraph
import com.dirzaaulia.formula1.navigation.NavRoute
import com.dirzaaulia.formula1.navigation.NavTab
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.FormulaTrackrTheme
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurfaceElevated
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.ui.component.ModernHeader
import com.dirzaaulia.formula1.ui.dialog.ConstructorDetailModal
import com.dirzaaulia.formula1.ui.dialog.DriverDetailModal
import com.dirzaaulia.formula1.ui.screen.splash.F1RaceStartSplashScreen

@Composable
fun FormulaTrackrApp() {
    FormulaTrackrTheme {
        var showSplash by remember { mutableStateOf(true) }

        val backStack = remember { mutableStateListOf<NavRoute>(NavRoute.Home) }
        val currentRoute = backStack.lastOrNull() ?: NavRoute.Home

        var selectedSeason by remember { mutableStateOf(2026) }
        var selectedDriverDetail by remember { mutableStateOf<Driver?>(null) }
        var selectedConstructorDetail by remember { mutableStateOf<Team?>(null) }

        val activeTab = when (currentRoute) {
            is NavRoute.Home -> NavTab.HOME
            is NavRoute.Telemetry -> NavTab.TELEMETRY
            is NavRoute.Calendar -> NavTab.CALENDAR
            is NavRoute.Standings -> NavTab.STANDINGS
            is NavRoute.Info -> NavTab.INFO
            is NavRoute.RaceDetail -> NavTab.CALENDAR
        }

        if (selectedDriverDetail != null) {
            DriverDetailModal(
                driver = selectedDriverDetail!!,
                onDismiss = { selectedDriverDetail = null }
            )
        }

        if (selectedConstructorDetail != null) {
            ConstructorDetailModal(
                team = selectedConstructorDetail!!,
                onDismiss = { selectedConstructorDetail = null }
            )
        }

        Box(modifier = Modifier.fillMaxSize().background(PitchBlack)) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val windowSizeClass = com.dirzaaulia.formula1.util.WindowSizeClass.calculateFromWidth(maxWidth)
                val isCompact = windowSizeClass.isCompact
                val isWidescreen = windowSizeClass.isWidescreen

                androidx.compose.runtime.CompositionLocalProvider(
                    com.dirzaaulia.formula1.util.LocalWindowSizeClass provides windowSizeClass
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        ModernHeader(
                            currentTab = activeTab,
                            onTabSelected = { tab ->
                                backStack.clear()
                                when (tab) {
                                    NavTab.HOME -> backStack.add(NavRoute.Home)
                                    NavTab.TELEMETRY -> backStack.add(NavRoute.Telemetry)
                                    NavTab.CALENDAR -> backStack.add(NavRoute.Calendar)
                                    NavTab.STANDINGS -> backStack.add(NavRoute.Standings)
                                    NavTab.INFO -> backStack.add(NavRoute.Info)
                                }
                            },
                            selectedSeason = selectedSeason,
                            onSeasonChanged = { selectedSeason = it },
                            isCompact = isCompact
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(PitchBlack),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            val isTelemetry = currentRoute is NavRoute.Telemetry
                            val isHome = currentRoute is NavRoute.Home
                            val maxContentWidth = if (isTelemetry || isWidescreen) 2400.dp else 1600.dp
                            val padH = if (isCompact) 10.dp else if (isTelemetry || isHome) 0.dp else 20.dp
                            val padV = if (isCompact) 8.dp else if (isTelemetry || isHome) 0.dp else 12.dp

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = maxContentWidth)
                                    .padding(
                                        horizontal = padH,
                                        vertical = padV
                                    )
                            ) {
                                NavGraph(
                                    currentRoute = currentRoute,
                                    selectedSeason = selectedSeason,
                                    onSeasonChanged = { selectedSeason = it },
                                    onNavigate = { route -> backStack.add(route) },
                                    onBack = {
                                        if (backStack.size > 1) {
                                            backStack.removeAt(backStack.lastIndex)
                                        }
                                    }
                                )
                            }
                        }

                        if (isCompact) {
                            FloatingGlassBottomNavBar(
                                currentTab = activeTab,
                                onTabSelected = { tab ->
                                    backStack.clear()
                                    when (tab) {
                                        NavTab.HOME -> backStack.add(NavRoute.Home)
                                        NavTab.TELEMETRY -> backStack.add(NavRoute.Telemetry)
                                        NavTab.CALENDAR -> backStack.add(NavRoute.Calendar)
                                        NavTab.STANDINGS -> backStack.add(NavRoute.Standings)
                                        NavTab.INFO -> backStack.add(NavRoute.Info)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            if (showSplash) {
                F1RaceStartSplashScreen(
                    onSplashFinished = { showSplash = false }
                )
            }
        }
    }
}

@Composable
private fun FloatingGlassBottomNavBar(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = GlassSurfaceElevated,
            border = BorderStroke(1.dp, GlassBorderActive),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTab.entries.forEach { tab ->
                    val selected = currentTab == tab

                    val bgModifier = if (selected) {
                        Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(F1RedSubtle, PitchBlack)
                                )
                            )
                    } else Modifier

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .then(bgModifier)
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (selected) F1Red else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = tab.title,
                            fontSize = 9.sp,
                            fontWeight = if (selected) FontWeight.Black else FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            color = if (selected) TextPure else TextMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}
