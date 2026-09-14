package com.dirzaaulia.formula1.ui.screen.telemetry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.LiveTimingDriverState
import com.dirzaaulia.formula1.model.OpenF1Driver
import com.dirzaaulia.formula1.model.OpenF1Lap
import com.dirzaaulia.formula1.model.OpenF1RaceControl
import com.dirzaaulia.formula1.model.OpenF1Stint
import com.dirzaaulia.formula1.model.OpenF1TeamRadio
import com.dirzaaulia.formula1.model.OpenF1Weather
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.model.SectorStatus
import com.dirzaaulia.formula1.network.F1LiveTimingService
import com.dirzaaulia.formula1.network.JolpicaNetworkService
import com.dirzaaulia.formula1.network.OpenF1Service
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.GlassBorder
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurface
import com.dirzaaulia.formula1.theme.HairlineBorder
import com.dirzaaulia.formula1.theme.MonoMuted
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurface
import com.dirzaaulia.formula1.theme.ObsidianSurfaceElevated
import com.dirzaaulia.formula1.theme.ObsidianVoid
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TelemetryGreen
import com.dirzaaulia.formula1.theme.TelemetryPurple
import com.dirzaaulia.formula1.theme.TelemetryYellow
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TyreHard
import com.dirzaaulia.formula1.theme.TyreIntermediate
import com.dirzaaulia.formula1.theme.TyreMedium
import com.dirzaaulia.formula1.theme.TyreSoft
import com.dirzaaulia.formula1.theme.TyreWet
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.ui.component.ShimmerCard
import com.dirzaaulia.formula1.ui.component.shimmerBrush
import com.dirzaaulia.formula1.ui.dialog.F1LoginDialog
import com.dirzaaulia.formula1.ui.screen.home.NextRaceCountdownCard
import com.dirzaaulia.formula1.util.ArchiveRaceOption
import com.dirzaaulia.formula1.util.TelemetryDataManager
import com.dirzaaulia.formula1.util.TelemetryPhysicsEngine
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl
import com.dirzaaulia.formula1.util.getTeamCarUrl
import com.dirzaaulia.formula1.util.parseUtcDateTimeToEpochMillis
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull

enum class TelemetryMode {
    LIVE,
    ARCHIVE
}

enum class ArchiveSubView {
    TIMING_TABLE,
    CIRCUIT_VIEW
}

enum class TelemetryLayoutMode {
    SPLIT,
    EXPAND_TIMING,
    EXPAND_CIRCUIT
}

@Composable
fun TelemetryScreen(selectedSeason: Int) {
    var mode by remember { mutableStateOf(TelemetryMode.ARCHIVE) }
    var archiveSubView by remember { mutableStateOf(ArchiveSubView.TIMING_TABLE) }
    var layoutMode by remember { mutableStateOf(TelemetryLayoutMode.SPLIT) }

    // Selected Race Option (Defaults to latest completed Grand Prix)
    var selectedRace by remember {
        mutableStateOf(TelemetryDataManager.OFFICIAL_2026_RACES.first())
    }
    var showRaceSelectorModal by remember { mutableStateOf(false) }
    var showF1LoginModal by remember { mutableStateOf(false) }
    var isLiveActive by remember { mutableStateOf(false) }
    var activeSessionName by remember { mutableStateOf("Spanish Grand Prix - Race") }
    var nextRoundRace by remember { mutableStateOf<Race?>(null) }

    // CRITICAL: Initial telemetry archive STARTS FROM LAP 1 (0.0f)
    var raceProgressFloat by remember { mutableFloatStateOf(0.0f) }
    var isPlaying by remember { mutableStateOf(false) }
    var replaySpeed by remember { mutableIntStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }

    // Selected driver for radar tracking / telemetry drawer
    var selectedDriverNumber by remember { mutableStateOf<Int?>(12) }

    val totalLaps = selectedRace.totalLaps
    val currentLap = (raceProgressFloat.toInt() + 1).coerceIn(1, totalLaps)
    val subLapFraction = (raceProgressFloat - raceProgressFloat.toInt()).coerceIn(0f, 0.999f)

    // Single hoisted animation transition for all sector micro-pills (avoids 66 separate clocks in WASM)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val sectorPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Telemetry collections (Preloaded with authentic 2026 race session data)
    var drivers by remember { mutableStateOf<List<OpenF1Driver>>(TelemetryDataManager.OFFICIAL_2026_DRIVERS) }
    var laps by remember { mutableStateOf<List<OpenF1Lap>>(TelemetryDataManager.getPreloadedLaps(selectedRace.sessionKey)) }
    var stints by remember { mutableStateOf<List<OpenF1Stint>>(TelemetryDataManager.getPreloadedStints(selectedRace.sessionKey)) }
    var raceControlMessages by remember { mutableStateOf<List<OpenF1RaceControl>>(TelemetryDataManager.PRELOADED_RACE_CONTROL) }
    var weatherDataList by remember { mutableStateOf<List<OpenF1Weather>>(emptyList()) }
    var teamRadioFeed by remember { mutableStateOf<List<OpenF1TeamRadio>>(TelemetryDataManager.PRELOADED_TEAM_RADIO) }
    var showRaceStatusDrawer by remember { mutableStateOf(false) }

    // Race Telemetry Data Loader (Fetches real session drivers, laps, stints, weather, flags, and radio)
    LaunchedEffect(selectedRace.sessionKey) {
        isLoading = true
        try {
            val fetchedDrivers = OpenF1Service.getDrivers(selectedRace.sessionKey)
            if (fetchedDrivers.isNotEmpty()) {
                drivers = fetchedDrivers
            }
            val fetchedLaps = OpenF1Service.getLaps(selectedRace.sessionKey)
            if (fetchedLaps.isNotEmpty()) {
                laps = fetchedLaps
            }
            val fetchedStints = OpenF1Service.getStints(selectedRace.sessionKey)
            if (fetchedStints.isNotEmpty()) {
                stints = fetchedStints
            }
            val fetchedRc = OpenF1Service.getRaceControl(selectedRace.sessionKey)
            if (fetchedRc.isNotEmpty()) {
                raceControlMessages = fetchedRc
            }
            val fetchedWeather = OpenF1Service.getWeather(selectedRace.sessionKey)
            if (fetchedWeather.isNotEmpty()) {
                weatherDataList = fetchedWeather
            }
            val fetchedRadio = OpenF1Service.getTeamRadio(selectedRace.sessionKey)
            if (fetchedRadio.isNotEmpty()) {
                teamRadioFeed = fetchedRadio
            }
        } catch (_: Throwable) {
        } finally {
            isLoading = false
        }
    }

    // Screen ready warm-up state for smooth 60/120fps Android navigation
    var isScreenReady by remember { mutableStateOf(false) }

    // 1. Asynchronous Live Session Check & Navigation Warm-up
    LaunchedEffect(Unit) {
        // Asynchronous delay allows Android navigation transition to complete with zero frame drop
        delay(60L)
        isScreenReady = true

        try {
            withTimeoutOrNull(3000L) {
                val (isLive, liveMeeting) = OpenF1Service.checkF1OfficialLiveStatus()
                // Only consider active if live session is actually streaming on track
                isLiveActive = isLive
                if (isLiveActive) {
                    mode = TelemetryMode.LIVE
                    selectedRace = TelemetryDataManager.LIVE_2026_RACE
                    if (liveMeeting.isNotBlank()) {
                        activeSessionName = liveMeeting
                    }
                } else {
                    // Race is finished or inactive: Keep on ARCHIVE mode with latest race
                    mode = TelemetryMode.ARCHIVE
                    selectedRace = TelemetryDataManager.OFFICIAL_2026_RACES.first()
                    activeSessionName = "2026 ${selectedRace.name} (${selectedRace.circuitShortName} - Archive)"
                }
            }

            if (!isLiveActive) {
                withTimeoutOrNull(2500L) {
                    nextRoundRace = JolpicaNetworkService.getNextUpcomingRace("2026")
                }
            }
        } catch (_: Throwable) {
            mode = TelemetryMode.ARCHIVE
        }
    }

    // 1.5 Sync active session and race when switching between LIVE and ARCHIVE modes
    LaunchedEffect(mode) {
        if (mode == TelemetryMode.LIVE) {
            selectedRace = TelemetryDataManager.LIVE_2026_RACE
            isPlaying = false
            try {
                withTimeoutOrNull(3000L) {
                    val (isLive, liveMeeting) = OpenF1Service.checkF1OfficialLiveStatus()
                    isLiveActive = isLive
                    if (liveMeeting.isNotBlank()) {
                        activeSessionName = liveMeeting
                    }
                }
                if (!isLiveActive) {
                    withTimeoutOrNull(2500L) {
                        nextRoundRace = JolpicaNetworkService.getNextUpcomingRace("2026")
                    }
                }
            } catch (_: Throwable) {
                isLiveActive = false
            }
        } else {
            // Archive defaults to last completed race
            selectedRace = TelemetryDataManager.OFFICIAL_2026_RACES.first()
            activeSessionName = "2026 ${selectedRace.name} (${selectedRace.circuitShortName} - Archive)"
            raceProgressFloat = 0.0f
            isPlaying = false
        }
    }

    // 2. Smooth Continuous Replay Engine (60 FPS, Zero Jumping)
    LaunchedEffect(isPlaying, replaySpeed, totalLaps, mode) {
        if (isPlaying && mode == TelemetryMode.ARCHIVE) {
            var lastTime = withFrameNanos { it }
            while (isActive && isPlaying) {
                withFrameNanos { now ->
                    val dtSeconds = (now - lastTime) / 1_000_000_000f
                    lastTime = now
                    val lapDurationSeconds = 18f / replaySpeed.toFloat()
                    val deltaProgress = dtSeconds / lapDurationSeconds

                    raceProgressFloat = (raceProgressFloat + deltaProgress).coerceAtMost(totalLaps.toFloat() - 0.001f)
                    if (raceProgressFloat >= totalLaps - 0.005f) {
                        isPlaying = false
                    }
                }
            }
        }
    }

    // 3. Compute Synchronized Driver Timing State with Continuous In-Lap Dynamic Standings
    val subLapBucket = (subLapFraction * 20).toInt()
    val timingDrivers = remember(drivers, laps, stints, currentLap, subLapBucket, selectedRace) {
        TelemetryDataManager.computeTimingStandings(
            currentLap = currentLap,
            subLapFraction = subLapFraction,
            totalLaps = totalLaps,
            driversList = drivers,
            lapsList = laps,
            stintsList = stints
        )
    }

    // 4. Dedicated Continuous Multi-Lap Circuit Progression (Decoupled from discrete timing table)
    val continuousCircuitPositions = remember(raceProgressFloat, laps, drivers, totalLaps) {
        TelemetryDataManager.computeContinuousCircuitTrackProgress(
            raceProgressFloat = raceProgressFloat,
            totalLaps = totalLaps,
            driversList = drivers,
            lapsList = laps
        )
    }

    // 5. Synchronized Environmental, Flag, Race Control, and Radio Telemetry State for currentLap
    val currentWeather = remember(weatherDataList, currentLap, totalLaps) {
        TelemetryDataManager.getWeatherForLap(weatherDataList, currentLap, totalLaps)
    }

    val activeRaceControl = remember(raceControlMessages, currentLap, totalLaps) {
        TelemetryDataManager.getRaceControlForLap(raceControlMessages, currentLap, totalLaps)
    }

    val activeTeamRadio = remember(teamRadioFeed, currentLap, totalLaps) {
        TelemetryDataManager.getTeamRadioForLap(teamRadioFeed, currentLap, totalLaps)
    }

    val (currentFlagText, currentFlagColor) = remember(activeRaceControl, currentLap, totalLaps, subLapFraction) {
        TelemetryDataManager.getFlagForLap(activeRaceControl, currentLap, totalLaps, subLapFraction)
    }

    // 5. Cater Real-Time Live Session Polling when LIVE mode is active
    LaunchedEffect(mode, isLiveActive) {
        if (mode == TelemetryMode.LIVE && isLiveActive) {
            while (isActive && mode == TelemetryMode.LIVE && isLiveActive) {
                try {
                    val liveSession = OpenF1Service.getLatestRaceSession()
                    if (liveSession != null) {
                        val fetchedDrivers = OpenF1Service.getDrivers(liveSession.sessionKey)
                        if (fetchedDrivers.isNotEmpty()) drivers = fetchedDrivers
                        val fetchedLaps = OpenF1Service.getLaps(liveSession.sessionKey)
                        if (fetchedLaps.isNotEmpty()) {
                            laps = fetchedLaps
                            val maxLap = fetchedLaps.maxOfOrNull { it.lapNumber } ?: 1
                            raceProgressFloat = (maxLap - 1).toFloat()
                        }
                    }
                } catch (_: Throwable) {
                }
                delay(3000L)
            }
        }
    }

    val scrollState = rememberScrollState()
    val focusedDriver = timingDrivers.firstOrNull { it.driverNumber == selectedDriverNumber }
        ?: timingDrivers.firstOrNull()
        ?: LiveTimingDriverState(
            position = 1,
            driverNumber = 1,
            code = "VER",
            fullName = "Max Verstappen",
            teamName = "Red Bull Racing",
            teamColor = Color(0xFF3671C6),
            gapToLeader = "LEADER",
            intervalAhead = "INTERVAL",
            compound = "SOFT",
            tyreAge = 5,
            currentLap = 12,
            lastLapTime = "1:21.046",
            s1Time = "26.412",
            s2Time = "27.189",
            s3Time = "27.445"
        )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(PitchBlack)
    ) {
        val isWidescreen = maxWidth >= 1180.dp

        if (!isScreenReady) {
            TelemetryShimmerSkeleton(isWidescreen = isWidescreen)
        } else {
            var showCircuitRadar by remember { mutableStateOf(true) }
            var showTimingTable by remember { mutableStateOf(true) }
            var showDriverTelemetry by remember { mutableStateOf(false) }
            var mobileViewTab by remember { mutableStateOf(ArchiveSubView.TIMING_TABLE) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        start = if (isWidescreen) 24.dp else 12.dp,
                        end = if (isWidescreen) 24.dp else 12.dp,
                        top = 16.dp,
                        bottom = 60.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. FLOATING DYNAMIC ISLAND PLAYER (iOS Style: Compact, Floating, Glassmorphic)
                DynamicIslandPlayer(
                    selectedRace = selectedRace,
                    mode = mode,
                    isLiveActive = isLiveActive,
                    currentLap = currentLap,
                    totalLaps = totalLaps,
                    raceProgressFloat = raceProgressFloat,
                    isPlaying = isPlaying,
                    replaySpeed = replaySpeed,
                    focusedDriver = focusedDriver,
                    showDriverTelemetry = showDriverTelemetry,
                    showTimingTable = showTimingTable,
                    showCircuitRadar = showCircuitRadar,
                    isWidescreen = isWidescreen,
                    onToggleDriverTelemetry = { showDriverTelemetry = !showDriverTelemetry },
                    onToggleTimingTable = { showTimingTable = !showTimingTable },
                    onToggleCircuitRadar = { showCircuitRadar = !showCircuitRadar },
                    onOpenRaceSelector = { showRaceSelectorModal = true },
                    onModeChange = { requestedMode ->
                        mode = requestedMode
                        if (requestedMode == TelemetryMode.LIVE) {
                            isPlaying = false
                            selectedRace = TelemetryDataManager.LIVE_2026_RACE
                        } else {
                            selectedRace = TelemetryDataManager.OFFICIAL_2026_RACES.first()
                            activeSessionName = "2026 ${selectedRace.name} (${selectedRace.circuitShortName} - Archive)"
                            raceProgressFloat = 0.0f
                            isPlaying = false
                        }
                    },
                    onProgressChange = { raceProgressFloat = it },
                    onPlayPauseToggle = { isPlaying = !isPlaying },
                    onSpeedCycle = {
                        replaySpeed = when (replaySpeed) {
                            1 -> 2
                            2 -> 4
                            4 -> 8
                            else -> 1
                        }
                    },
                    onStepLaps = { step: Int ->
                        raceProgressFloat = (raceProgressFloat.toInt() + step).toFloat().coerceIn(0f, totalLaps.toFloat() - 1f)
                    },
                    mobileViewTab = mobileViewTab,
                    onMobileViewTabChange = { mobileViewTab = it },
                    onOpenF1Login = { showF1LoginModal = true }
                )

            if (mode == TelemetryMode.LIVE && !isLiveActive) {
                // If there is no active session on track, show the countdown card to next session
                val activeNextRace = nextRoundRace ?: Race(
                    raceName = "Azerbaijan Grand Prix",
                    round = 15,
                    date = "2026-09-20",
                    time = "11:00:00Z",
                    circuit = com.dirzaaulia.formula1.model.Circuit(
                        circuitId = "baku",
                        circuitName = "Baku City Circuit",
                        country = "Azerbaijan",
                        location = "Baku"
                    ),
                    schedule = com.dirzaaulia.formula1.model.RaceSchedule(
                        fp1 = com.dirzaaulia.formula1.model.RaceDate("2026-09-18", "09:30:00Z"),
                        fp2 = com.dirzaaulia.formula1.model.RaceDate("2026-09-18", "13:00:00Z"),
                        fp3 = com.dirzaaulia.formula1.model.RaceDate("2026-09-19", "08:30:00Z"),
                        qualy = com.dirzaaulia.formula1.model.RaceDate("2026-09-19", "12:00:00Z"),
                        race = com.dirzaaulia.formula1.model.RaceDate("2026-09-20", "11:00:00Z")
                    )
                )
                LiveIdleCountdownSection(
                    nextRace = activeNextRace,
                    onSwitchToReplay = {
                        mode = TelemetryMode.ARCHIVE
                        selectedRace = TelemetryDataManager.OFFICIAL_2026_RACES.first()
                        activeSessionName = "2026 ${selectedRace.name} (${selectedRace.circuitShortName} - Archive)"
                        raceProgressFloat = 0.0f
                        isPlaying = false
                    },
                    onOpenF1Login = { showF1LoginModal = true },
                    isCompact = !isWidescreen
                )
            } else {
                // 2. DETAILED DRIVER TELEMETRY STAGE (Revealed when user taps a driver)
                AnimatedVisibility(visible = showDriverTelemetry) {
                    FocusedDriverTelemetryStage(
                        driver = focusedDriver,
                        subLapFraction = subLapFraction,
                        currentLap = currentLap,
                        weather = currentWeather,
                        isWidescreen = isWidescreen,
                        onClose = { showDriverTelemetry = false },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 2.5 RACE STATUS COMMAND STRIP (Track Flag, Weather, Race Control, Team Radio)
                RaceStatusControlStrip(
                    raceControl = activeRaceControl,
                    weather = currentWeather,
                    teamRadio = activeTeamRadio,
                    flagText = currentFlagText,
                    flagColor = currentFlagColor,
                    currentLap = currentLap,
                    isWidescreen = isWidescreen,
                    isExpanded = showRaceStatusDrawer,
                    onToggleExpand = { showRaceStatusDrawer = !showRaceStatusDrawer }
                )

            // 3. MAIN FOCUS: TIMING TABLE & CIRCUIT RADAR
            if (showTimingTable && showCircuitRadar) {
                if (isWidescreen) {
                    // Widescreen Split: Timing table gets primary focus (58%), Circuit gets 42%
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        FullTimingTowerCard(
                            timingDrivers = timingDrivers,
                            currentLap = currentLap,
                            totalLaps = totalLaps,
                            subLapFraction = subLapFraction,
                            selectedDriverNumber = selectedDriverNumber,
                            onDriverSelect = { num ->
                                selectedDriverNumber = num
                                showDriverTelemetry = true
                            },
                            onToggleVisibility = { showTimingTable = false },
                            pulseAlpha = sectorPulseAlpha,
                            isPlaying = isPlaying,
                            modifier = Modifier.weight(0.58f)
                        )

                        ExpansiveCircuitMapCard(
                            selectedRace = selectedRace,
                            timingDrivers = timingDrivers,
                            subLapFraction = subLapFraction,
                            selectedDriverNumber = selectedDriverNumber,
                            onDriverSelect = { num ->
                                selectedDriverNumber = num
                                showDriverTelemetry = true
                            },
                            onToggleVisibility = { showCircuitRadar = false },
                            carPositionsMap = continuousCircuitPositions,
                            modifier = Modifier.weight(0.42f)
                        )
                    }
                } else {
                    // Mobile View (Directly switched via DynamicIslandPlayer pill)
                    if (mobileViewTab == ArchiveSubView.TIMING_TABLE) {
                        FullTimingTowerCard(
                            timingDrivers = timingDrivers,
                            currentLap = currentLap,
                            totalLaps = totalLaps,
                            subLapFraction = subLapFraction,
                            selectedDriverNumber = selectedDriverNumber,
                            onDriverSelect = { num ->
                                selectedDriverNumber = num
                                showDriverTelemetry = true
                            },
                            onToggleVisibility = null,
                            pulseAlpha = sectorPulseAlpha,
                            isPlaying = isPlaying,
                            isCompact = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        ExpansiveCircuitMapCard(
                            selectedRace = selectedRace,
                            timingDrivers = timingDrivers,
                            subLapFraction = subLapFraction,
                            selectedDriverNumber = selectedDriverNumber,
                            onDriverSelect = { num ->
                                selectedDriverNumber = num
                                showDriverTelemetry = true
                            },
                            onToggleVisibility = null,
                            carPositionsMap = continuousCircuitPositions,
                            isCompact = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else if (showTimingTable) {
                // Timing Table Only - Full Width Maximum Focus
                FullTimingTowerCard(
                    timingDrivers = timingDrivers,
                    currentLap = currentLap,
                    totalLaps = totalLaps,
                    subLapFraction = subLapFraction,
                    selectedDriverNumber = selectedDriverNumber,
                    onDriverSelect = { num ->
                        selectedDriverNumber = num
                        showDriverTelemetry = true
                    },
                    onToggleVisibility = { showTimingTable = false },
                    pulseAlpha = sectorPulseAlpha,
                    isPlaying = isPlaying,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (showCircuitRadar) {
                // Circuit Radar Only - Full Width
                ExpansiveCircuitMapCard(
                    selectedRace = selectedRace,
                    timingDrivers = timingDrivers,
                    subLapFraction = subLapFraction,
                    selectedDriverNumber = selectedDriverNumber,
                    onDriverSelect = { num ->
                        selectedDriverNumber = num
                        showDriverTelemetry = true
                    },
                    onToggleVisibility = { showCircuitRadar = false },
                    carPositionsMap = continuousCircuitPositions,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Both Hidden: Minimal banner with buttons to restore views
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xCC0E1118)),
                    border = BorderStroke(1.dp, Color(0x20FFFFFF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("All telemetry panels are hidden.", color = Color(0xFF8E95A5), fontSize = 13.sp)
                        Spacer(Modifier.width(16.dp))
                        Button(
                            onClick = { showTimingTable = true },
                            colors = ButtonDefaults.buttonColors(containerColor = F1Red)
                        ) {
                            Text("Show Timing Table")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { showCircuitRadar = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x30FFFFFF))
                        ) {
                            Text("Show Circuit Radar")
                        }
                    }
                }
            }
        }
    }
}

    // ARCHIVE RACE SELECTOR MODAL DIALOG
    if (showRaceSelectorModal) {
        ArchiveRaceSelectorBottomSheet(
            races = TelemetryDataManager.OFFICIAL_2026_RACES,
            selectedRace = selectedRace,
            onSelectRace = { race ->
                selectedRace = race
                raceProgressFloat = 0.0f // Reset to Lap 1
                isPlaying = false
                showRaceSelectorModal = false
            },
            onDismiss = { showRaceSelectorModal = false }
        )
    }

    if (showF1LoginModal) {
        F1LoginDialog(
            onDismiss = { showF1LoginModal = false },
            onTokenReceived = { token ->
                showF1LoginModal = false
                isLiveActive = true
            }
        )
    }

    }
}

// ══════════════════════════════════════════════════════════════
//  LIVE IDLE & COUNTDOWN SCHEDULE SECTION
// ══════════════════════════════════════════════════════════════

@Composable
private fun LiveIdleCountdownSection(
    nextRace: Race,
    onSwitchToReplay: () -> Unit,
    onOpenF1Login: () -> Unit,
    isCompact: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Official FIA Live Feed Standby Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xDD0D111A),
            border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(F1Red.copy(alpha = 0.5f), Color(0x20FFFFFF)))),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(F1RedSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(F1Red)
                            )
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "LIVE TELEMETRY: STANDBY",
                                    color = MonoWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0x30FFFFFF))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "AWAITING TRACK ACTION",
                                        color = MonoMuted,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Live timing, micro sectors, and intervals will stream automatically when cars take to the track.",
                                color = MonoSilver,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Actions Row: Connect F1 & 2026 Replay
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onOpenF1Login,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFF1E2330)
                        ),
                        border = BorderStroke(1.dp, if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0x40FFFFFF)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (F1LiveTimingService.isConnected()) Icons.Default.Check else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (F1LiveTimingService.isConnected()) PitchBlack else MonoWhite,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (F1LiveTimingService.isConnected()) "F1 LINKED" else "CONNECT F1",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = if (F1LiveTimingService.isConnected()) PitchBlack else MonoWhite
                        )
                    }

                    Button(
                        onClick = onSwitchToReplay,
                        colors = ButtonDefaults.buttonColors(containerColor = F1Red),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "2026 REPLAY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Official Next Grand Prix Countdown Card
        NextRaceCountdownCard(
            race = nextRace,
            onCalendarClick = onSwitchToReplay,
            isCompact = isCompact
        )
    }
}

// ══════════════════════════════════════════════════════════════
//  TELEMETRY SHIMMER SKELETON (FLUID ANDROID TRANSITION)
// ══════════════════════════════════════════════════════════════

@Composable
private fun TelemetryShimmerSkeleton(isWidescreen: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = if (isWidescreen) 24.dp else 12.dp,
                end = if (isWidescreen) 24.dp else 12.dp,
                top = 16.dp,
                bottom = 60.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Floating Dynamic Island Player skeleton
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color(0xF00D1018),
            border = BorderStroke(1.dp, Color(0x20FFFFFF)),
            modifier = Modifier.fillMaxWidth().height(if (isWidescreen) 56.dp else 88.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().shimmerBrush())
        }

        // Race Status Control Strip skeleton
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ObsidianSurface,
            border = BorderStroke(1.dp, GlassBorder),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().shimmerBrush())
        }

        // Main content skeleton
        if (isWidescreen) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShimmerCard(modifier = Modifier.weight(0.58f), height = 520.dp)
                ShimmerCard(modifier = Modifier.weight(0.42f), height = 520.dp)
            }
        } else {
            ShimmerCard(modifier = Modifier.fillMaxWidth(), height = 500.dp)
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  DYNAMIC ISLAND TELEMETRY & DRIVER STAGE COMPOSABLES
// ══════════════════════════════════════════════════════════════

@Composable
private fun DynamicIslandPlayer(
    selectedRace: ArchiveRaceOption,
    mode: TelemetryMode,
    isLiveActive: Boolean,
    currentLap: Int,
    totalLaps: Int,
    raceProgressFloat: Float,
    isPlaying: Boolean,
    replaySpeed: Int,
    focusedDriver: LiveTimingDriverState,
    showDriverTelemetry: Boolean,
    showTimingTable: Boolean,
    showCircuitRadar: Boolean,
    isWidescreen: Boolean,
    onToggleDriverTelemetry: () -> Unit,
    onToggleTimingTable: () -> Unit,
    onToggleCircuitRadar: () -> Unit,
    onOpenRaceSelector: () -> Unit,
    onModeChange: (TelemetryMode) -> Unit,
    onProgressChange: (Float) -> Unit,
    onPlayPauseToggle: () -> Unit,
    onSpeedCycle: () -> Unit,
    onStepLaps: (Int) -> Unit,
    mobileViewTab: ArchiveSubView = ArchiveSubView.TIMING_TABLE,
    onMobileViewTabChange: (ArchiveSubView) -> Unit = {},
    onOpenF1Login: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color(0xF00D1018),
        border = BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                listOf(
                    Color(0x35CCFF00),
                    Color(0x20FFFFFF),
                    focusedDriver.teamColor.copy(alpha = 0.5f)
                )
            )
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        if (isWidescreen) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT: Session Mode & Grand Prix Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // LIVE / REPLAY pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF141722))
                            .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(20.dp))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (mode == TelemetryMode.LIVE) F1Red else Color.Transparent)
                                .clickable { onModeChange(TelemetryMode.LIVE) }
                                .padding(horizontal = 11.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "LIVE",
                                color = if (mode == TelemetryMode.LIVE) Color.White else Color(0xFF8E95A5),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (mode == TelemetryMode.ARCHIVE) Color(0xFF00E5FF) else Color.Transparent)
                                .clickable { onModeChange(TelemetryMode.ARCHIVE) }
                                .padding(horizontal = 11.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "REPLAY",
                                color = if (mode == TelemetryMode.ARCHIVE) PitchBlack else Color(0xFF8E95A5),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Clickable Race Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x20FFFFFF))
                            .border(1.dp, Color(0x35FFFFFF), RoundedCornerShape(20.dp))
                            .clickable { onOpenRaceSelector() }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(selectedRace.country),
                            contentDescription = selectedRace.country,
                            modifier = Modifier
                                .height(13.dp)
                                .width(20.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                        Text(
                            text = "${selectedRace.name} • L$currentLap/$totalLaps",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Grand Prix",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Lap Counter Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (mode == TelemetryMode.LIVE && !isLiveActive) Color(0x22FF5252) else Color(0x15FFFFFF))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (mode == TelemetryMode.LIVE && !isLiveActive) "AWAITING SESSION" else "LAP $currentLap/$totalLaps",
                            color = if (mode == TelemetryMode.LIVE && !isLiveActive) Color(0xFFFF5252) else Color(0xFFCCFF00),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // CENTER: Dynamic Island Focused Driver Capsule (Clickable to reveal telemetry)
                if (mode == TelemetryMode.LIVE && !isLiveActive) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFB300))
                        )
                        Text(
                            text = "AWAITING TRACK ACTION • COUNTDOWN ACTIVE",
                            color = Color(0xFFD0D5DD),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (showDriverTelemetry) Color(0x35FFFFFF) else Color(0x1AFFFFFF))
                            .border(1.dp, if (showDriverTelemetry) Color(0xFFCCFF00) else focusedDriver.teamColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                            .clickable { onToggleDriverTelemetry() }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(focusedDriver.teamColor.copy(alpha = 0.35f))
                                .border(1.dp, focusedDriver.teamColor.copy(alpha = 0.8f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = focusedDriver.code.take(2),
                                color = Color.White,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            FormulaTrackrImage(
                                url = getDriverHeadshotUrl(focusedDriver.code),
                                contentDescription = focusedDriver.fullName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFFFD700))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "P${focusedDriver.position}",
                                color = Color.Black,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = focusedDriver.fullName,
                            color = Color.White,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(focusedDriver.teamColor)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "#${focusedDriver.driverNumber}",
                                color = Color.Black,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // Telemetry toggle indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (showDriverTelemetry) Color(0xFFCCFF00) else Color(0x20FFFFFF))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = if (showDriverTelemetry) Color.Black else Color(0xFFD0D5DD),
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = if (showDriverTelemetry) "TELEMETRY ON" else "TELEMETRY",
                                color = if (showDriverTelemetry) Color.Black else Color(0xFFD0D5DD),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // RIGHT: View Toggles & Replay Scrubber Controls
                if (mode == TelemetryMode.LIVE && !isLiveActive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onModeChange(TelemetryMode.ARCHIVE) },
                            colors = ButtonDefaults.buttonColors(containerColor = F1Red),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("WATCH ARCHIVE (MONZA)", fontSize = 10.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // View Toggle: Timing Tower
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (showTimingTable) Color(0x3500E676) else Color(0x18FFFFFF))
                                .clickable { onToggleTimingTable() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (showTimingTable) "TABLE" else "TABLE (OFF)",
                                color = if (showTimingTable) Color(0xFF00E676) else Color(0xFF8E95A5),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // View Toggle: Circuit Radar
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (showCircuitRadar) Color(0x3500E676) else Color(0x18FFFFFF))
                                .clickable { onToggleCircuitRadar() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (showCircuitRadar) "RADAR" else "RADAR (OFF)",
                                color = if (showCircuitRadar) Color(0xFF00E676) else Color(0xFF8E95A5),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        if (mode == TelemetryMode.ARCHIVE) {
                            // Step -1L
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x20FFFFFF))
                                    .clickable { onStepLaps(-1) }
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                            ) {
                                Text("-1L", color = Color(0xFFD0D5DD), fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }

                            // Play/Pause
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlaying) F1Red else Color(0x35FFFFFF))
                                    .clickable { onPlayPauseToggle() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Step +1L
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x20FFFFFF))
                                    .clickable { onStepLaps(1) }
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                            ) {
                                Text("+1L", color = Color(0xFFD0D5DD), fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }

                            // Speed
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x20FFFFFF))
                                    .clickable { onSpeedCycle() }
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                            ) {
                                Text("${replaySpeed}X", color = Color(0xFFFFC700), fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                            }

                            // Slider
                            Slider(
                                value = raceProgressFloat,
                                onValueChange = onProgressChange,
                                valueRange = 0f..(totalLaps - 1).toFloat().coerceAtLeast(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFFCCFF00),
                                    activeTrackColor = Color(0xFFCCFF00),
                                    inactiveTrackColor = Color(0x30FFFFFF)
                                ),
                                modifier = Modifier.width(110.dp).height(20.dp)
                            )
                        } else {
                            // LIVE mode track status badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isLiveActive) TelemetryGreen.copy(alpha = 0.2f) else F1Red.copy(alpha = 0.15f))
                                    .border(1.dp, if (isLiveActive) TelemetryGreen else F1Red.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (isLiveActive) TelemetryGreen else F1Red))
                                Text(
                                    text = if (isLiveActive) "LIVE ON TRACK" else "LIVE STANDBY",
                                    color = if (isLiveActive) TelemetryGreen else F1Red,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        // F1 Login Pill (Always visible & accessible in both modes)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (F1LiveTimingService.isConnected()) Color(0x2200E676) else Color(0x18FFFFFF))
                                .border(1.dp, if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0x30FFFFFF), RoundedCornerShape(14.dp))
                                .clickable { onOpenF1Login() }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = if (F1LiveTimingService.isConnected()) Icons.Default.Check else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFFCAD1E0),
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = if (F1LiveTimingService.isConnected()) "F1 LINKED" else "F1 LOGIN",
                                color = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFFCAD1E0),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        } else {
            // NARROW / MOBILE RESPONSIVE COMPACT ISLAND
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Mode & GP
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF141722))
                                .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(14.dp))
                                .padding(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (mode == TelemetryMode.LIVE) F1Red else Color.Transparent)
                                    .clickable { onModeChange(TelemetryMode.LIVE) }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    "LIVE",
                                    color = if (mode == TelemetryMode.LIVE) Color.White else Color(0xFF8E95A5),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (mode == TelemetryMode.ARCHIVE) Color(0xFF00E5FF) else Color.Transparent)
                                    .clickable { onModeChange(TelemetryMode.ARCHIVE) }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    "REPLAY",
                                    color = if (mode == TelemetryMode.ARCHIVE) PitchBlack else Color(0xFF8E95A5),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x22FFFFFF))
                                .border(1.dp, Color(0x35FFFFFF), RoundedCornerShape(12.dp))
                                .clickable { onOpenRaceSelector() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            FormulaTrackrImage(
                                url = getCountryFlagUrl(selectedRace.country),
                                contentDescription = selectedRace.country,
                                modifier = Modifier
                                    .height(11.dp)
                                    .width(16.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Text(
                                text = "${selectedRace.name} • L$currentLap/$totalLaps",
                                color = Color.White,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Grand Prix",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    // Right: In LIVE mode show F1 LOGIN button, or Driver Pill in ARCHIVE mode
                    if (mode == TelemetryMode.LIVE) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (F1LiveTimingService.isConnected()) Color(0x2200E676) else Color(0x22FFB300))
                                .border(1.dp, if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0x44FFB300), RoundedCornerShape(12.dp))
                                .clickable { onOpenF1Login() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (F1LiveTimingService.isConnected()) Icons.Default.Check else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFFFFB300),
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = if (F1LiveTimingService.isConnected()) "F1 LINKED" else "F1 LOGIN",
                                color = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFFFFB300),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (showDriverTelemetry) Color(0x35FFFFFF) else Color(0x18FFFFFF))
                                .clickable { onToggleDriverTelemetry() }
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "P${focusedDriver.position} ${focusedDriver.code}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (showDriverTelemetry) "^" else "v",
                                color = Color(0xFFCCFF00),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Row 2: View Toggles & Replay Bar / Live Status (Always visible)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Compact Subview Switcher: TIMING vs CIRCUIT
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF141722))
                            .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(16.dp))
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isTable = mobileViewTab == ArchiveSubView.TIMING_TABLE
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isTable) F1Red else Color.Transparent)
                                .clickable { onMobileViewTabChange(ArchiveSubView.TIMING_TABLE) }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Leaderboard,
                                    contentDescription = null,
                                    tint = if (isTable) Color.White else Color(0xFF8E95A5),
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "TIMING",
                                    color = if (isTable) Color.White else Color(0xFF8E95A5),
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!isTable) F1Red else Color.Transparent)
                                .clickable { onMobileViewTabChange(ArchiveSubView.CIRCUIT_VIEW) }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = if (!isTable) Color.White else Color(0xFF8E95A5),
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "CIRCUIT",
                                    color = if (!isTable) Color.White else Color(0xFF8E95A5),
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    if (mode == TelemetryMode.ARCHIVE) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0x18FFFFFF)).clickable { onStepLaps(-1) }.padding(horizontal = 6.dp, vertical = 3.dp)) {
                                Text("-1L", color = Color(0xFFD0D5DD), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(if (isPlaying) F1Red else Color(0x35FFFFFF)).clickable { onPlayPauseToggle() }, contentAlignment = Alignment.Center) {
                                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0x18FFFFFF)).clickable { onStepLaps(1) }.padding(horizontal = 6.dp, vertical = 3.dp)) {
                                Text("+1L", color = Color(0xFFD0D5DD), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0x18FFFFFF)).clickable { onSpeedCycle() }.padding(horizontal = 6.dp, vertical = 3.dp)) {
                                Text("${replaySpeed}X", color = Color(0xFFFFC700), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isLiveActive) TelemetryGreen.copy(alpha = 0.2f) else F1Red.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(if (isLiveActive) TelemetryGreen else F1Red))
                                Text(
                                    text = if (isLiveActive) "LIVE" else "STANDBY",
                                    color = if (isLiveActive) TelemetryGreen else F1Red,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (F1LiveTimingService.isConnected()) Color(0x2200E676) else Color(0x18FFFFFF))
                                    .border(1.dp, if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0x30FFFFFF), RoundedCornerShape(10.dp))
                                    .clickable { onOpenF1Login() }
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = if (F1LiveTimingService.isConnected()) Icons.Default.Check else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFFCAD1E0),
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = if (F1LiveTimingService.isConnected()) "F1 LINKED" else "F1 LOGIN",
                                    color = if (F1LiveTimingService.isConnected()) Color(0xFF00E676) else Color(0xFFCAD1E0),
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusedDriverTelemetryStage(
    driver: LiveTimingDriverState,
    subLapFraction: Float,
    currentLap: Int,
    weather: OpenF1Weather = OpenF1Weather(),
    isWidescreen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xCC0E1118),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(Color(0x40CCFF00), Color(0x15FFFFFF)))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(driver.teamColor.copy(alpha = 0.35f))
                            .border(1.dp, driver.teamColor.copy(alpha = 0.8f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = driver.code.take(2),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        FormulaTrackrImage(
                            url = getDriverHeadshotUrl(driver.code),
                            contentDescription = driver.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "P${driver.position} ${driver.fullName}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(driver.teamColor)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "#${driver.driverNumber} ${driver.code}",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                        Text(
                            text = "${driver.teamName} • LIVE TELEMETRY & STRATEGY",
                            color = Color(0xFF8E95A5),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Car thumbnail with resilient fallback
                    Box(modifier = Modifier.height(34.dp).width(110.dp), contentAlignment = Alignment.Center) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = driver.teamColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, driver.teamColor.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = getTeamAbbreviation(driver.teamName),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = driver.teamColor
                                )
                            }
                        }
                        FormulaTrackrImage(
                            url = getTeamCarUrl(driver.teamName),
                            contentDescription = driver.teamName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Close Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x20FFFFFF))
                            .clickable { onClose() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("CLOSE", color = Color(0xFFD0D5DD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Cards Body
            if (isWidescreen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    McLarenLiveTelemetryCard(
                        driver = driver,
                        subLapFraction = subLapFraction,
                        modifier = Modifier.weight(1.1f)
                    )
                    McLarenStintOverviewCard(
                        driver = driver,
                        currentLap = currentLap,
                        weather = weather,
                        modifier = Modifier.weight(1f)
                    )
                    McLarenTyrePerformanceCard(
                        driver = driver,
                        currentLap = currentLap,
                        subLapFraction = subLapFraction,
                        modifier = Modifier.weight(0.9f)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    McLarenLiveTelemetryCard(
                        driver = driver,
                        subLapFraction = subLapFraction,
                        modifier = Modifier.fillMaxWidth()
                    )
                    McLarenStintOverviewCard(
                        driver = driver,
                        currentLap = currentLap,
                        weather = weather,
                        modifier = Modifier.fillMaxWidth()
                    )
                    McLarenTyrePerformanceCard(
                        driver = driver,
                        currentLap = currentLap,
                        subLapFraction = subLapFraction,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun McLarenLiveTelemetryCard(
    driver: LiveTimingDriverState,
    subLapFraction: Float,
    modifier: Modifier = Modifier
) {
    val telemetry = TelemetryPhysicsEngine.calculateLiveTelemetry(subLapFraction)
    val speed = telemetry.speedKmh
    val rpm = telemetry.rpm
    val gear = telemetry.gear
    val throttle = telemetry.throttlePercent
    val brake = telemetry.brakePercent

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xCC0E1118),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(Color(0x35FFFFFF), Color(0x0CFFFFFF)))),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: • LIVE TELEMETRY | CAR 03
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFCCFF00)))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "LIVE TELEMETRY",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (speed > 250) Color(0x3000E676) else Color(0x18FFFFFF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (speed > 250) "DRS ACTIVE" else "DRS AVAIL",
                            color = if (speed > 250) Color(0xFF00E676) else Color(0xFF8E95A5),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = "CAR ${driver.driverNumber.toString().padStart(2, '0')}",
                        color = Color(0xFF8E95A5),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Primary Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speed
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$speed",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.width(6.dp))
                    Column(modifier = Modifier.padding(bottom = 4.dp)) {
                        Text("KM/H", color = Color(0xFF8E95A5), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Speed", color = Color(0xFF6B7280), fontSize = 10.sp)
                    }
                }

                // RPM
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$rpm",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.width(6.dp))
                    Column(modifier = Modifier.padding(bottom = 4.dp)) {
                        Text("RPM", color = Color(0xFF8E95A5), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Engine", color = Color(0xFF6B7280), fontSize = 10.sp)
                    }
                }

                // Gear indicator pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF161B26))
                        .border(1.dp, Color(0x35FFFFFF), RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("GEAR", color = Color(0xFF8E95A5), fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text("$gear", color = Color(0xFFFFD700), fontSize = 20.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }

                // Throttle / Brake mini bars
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("THR", color = Color(0xFF8E95A5), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Box(modifier = Modifier.width(50.dp).height(6.dp).background(Color(0x20FFFFFF), RoundedCornerShape(3.dp))) {
                            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(throttle / 100f).background(Color(0xFF00E676), RoundedCornerShape(3.dp)))
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("BRK", color = Color(0xFF8E95A5), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        Box(modifier = Modifier.width(50.dp).height(6.dp).background(Color(0x20FFFFFF), RoundedCornerShape(3.dp))) {
                            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(brake / 100f).background(F1Red, RoundedCornerShape(3.dp)))
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Speed Chart Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(Color(0x18000000), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    val points = listOf(
                        0.0f to 0.45f,
                        0.15f to 0.35f,
                        0.28f to 0.50f,
                        0.40f to 0.30f,
                        0.58f to 0.85f, // Apex turn 10
                        0.75f to 0.40f,
                        0.90f to 0.32f,
                        1.0f to 0.40f
                    )

                    // Gradient area fill
                    val fillPath = Path()
                    fillPath.moveTo(0f, points[0].second * h)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val cx = (prev.first + curr.first) / 2f * w
                        fillPath.cubicTo(
                            cx, prev.second * h,
                            cx, curr.second * h,
                            curr.first * w, curr.second * h
                        )
                    }
                    fillPath.lineTo(w, h)
                    fillPath.lineTo(0f, h)
                    fillPath.close()

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0x35CCFF00), Color.Transparent),
                            startY = 0f,
                            endY = h
                        )
                    )

                    // Line stroke
                    val strokePath = Path()
                    strokePath.moveTo(0f, points[0].second * h)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val cx = (prev.first + curr.first) / 2f * w
                        strokePath.cubicTo(
                            cx, prev.second * h,
                            cx, curr.second * h,
                            curr.first * w, curr.second * h
                        )
                    }

                    drawPath(
                        path = strokePath,
                        color = Color(0xFFCCFF00),
                        style = Stroke(width = 2.4f)
                    )

                    // Apex Point marker
                    val apexX = 0.58f * w
                    val apexY = 0.85f * h
                    drawCircle(color = Color(0xFFCCFF00), radius = 5f, center = Offset(apexX, apexY))
                    drawCircle(color = Color.White, radius = 2.5f, center = Offset(apexX, apexY))

                    // Sector dividers
                    drawLine(
                        color = Color(0x25FFFFFF),
                        start = Offset(0.33f * w, 0f),
                        end = Offset(0.33f * w, h),
                        strokeWidth = 1f
                    )
                    drawLine(
                        color = Color(0x25FFFFFF),
                        start = Offset(0.66f * w, 0f),
                        end = Offset(0.66f * w, h),
                        strokeWidth = 1f
                    )
                }

                // Turn 10 tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(x = 18.dp, y = 4.dp)
                        .background(Color(0xFF1F2430), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("TURN 10", color = Color(0xFFCCFF00), fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }

                // Sector labels
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("S1: 26.41s", color = Color(0xFF8E95A5), fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
                    Text("S2: 27.18s", color = Color(0xFFCCFF00), fontSize = 8.5.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Text("S3: 27.44s", color = Color(0xFF8E95A5), fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
private fun McLarenStintOverviewCard(
    driver: LiveTimingDriverState,
    currentLap: Int,
    weather: OpenF1Weather = OpenF1Weather(),
    modifier: Modifier = Modifier
) {
    val compoundUpper = driver.compound.uppercase().ifBlank { "MEDIUM" }
    val (tyreDotColor, tyreCode, tyreFullName) = when (compoundUpper) {
        "SOFT" -> Triple(TyreSoft, "S", "Soft")
        "HARD" -> Triple(TyreHard, "H", "Hard")
        "INTERMEDIATE" -> Triple(TyreIntermediate, "I", "Intermediate")
        "WET" -> Triple(TyreWet, "W", "Wet")
        else -> Triple(TyreMedium, "M", "Medium")
    }

    val stintLaps = if (driver.tyreAge > 0) driver.tyreAge else (currentLap % 22).coerceAtLeast(1)
    val bestLap = driver.bestLapTime.ifBlank { "1:13.526" }

    val stintLifespan = when (compoundUpper) {
        "SOFT" -> 18
        "HARD" -> 36
        "INTERMEDIATE" -> 25
        "WET" -> 28
        else -> 26
    }
    val pitLapTarget = (currentLap + (stintLifespan - stintLaps)).coerceAtLeast(currentLap + 1)
    val pitWindowText = "LAP ${pitLapTarget - 2} - ${pitLapTarget + 2}"
    val estLapsRemaining = (pitLapTarget - currentLap).coerceAtLeast(1)

    val degText = when {
        stintLaps < (stintLifespan * 0.4f) -> "TYRE DEG: LOW"
        stintLaps < (stintLifespan * 0.75f) -> "TYRE DEG: MED"
        else -> "TYRE DEG: HIGH"
    }
    val degColor = when {
        stintLaps < (stintLifespan * 0.4f) -> Color(0xFF00E676)
        stintLaps < (stintLifespan * 0.75f) -> TelemetryYellow
        else -> F1Red
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xCC0E1118),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(Color(0x35FFFFFF), Color(0x0CFFFFFF)))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STINT OVERVIEW",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = degText,
                    color = degColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Current Stint
                Column(modifier = Modifier.weight(1f)) {
                    Text("Current Stint", color = Color(0xFF8E95A5), fontSize = 10.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$stintLaps",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("LAPS", color = Color(0xFF8E95A5), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 3.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(tyreDotColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tyreCode, color = if (compoundUpper == "SOFT" || compoundUpper == "WET") Color.White else Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(Modifier.width(5.dp))
                        Text(tyreFullName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0x18FFFFFF)))
                Spacer(Modifier.width(12.dp))

                // Best Lap
                Column(modifier = Modifier.weight(1.1f)) {
                    Text("Best Lap", color = Color(0xFF8E95A5), fontSize = 10.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = bestLap,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("DIFF TO LEADER -0.218s", color = Color(0xFFCCFF00), fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }

                Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0x18FFFFFF)))
                Spacer(Modifier.width(12.dp))

                // Next Pit Window
                Column(modifier = Modifier.weight(1.1f)) {
                    Text("Next Pit Window", color = Color(0xFF8E95A5), fontSize = 10.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = pitWindowText,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("EST. $estLapsRemaining LAPS", color = Color(0xFF00E676), fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(Modifier.height(14.dp))

            // Weather telemetry bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x15FFFFFF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TRACK: ${weather.trackTemperature}°C", color = Color(0xFFD0D5DD), fontSize = 9.5.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Text("AIR: ${weather.airTemperature}°C", color = Color(0xFF8E95A5), fontSize = 9.5.sp, fontFamily = FontFamily.Monospace)
                Text("HUMIDITY: ${weather.humidity.toInt()}%", color = Color(0xFF8E95A5), fontSize = 9.5.sp, fontFamily = FontFamily.Monospace)
                Text(if (weather.rainfall > 0) "RAIN: WET" else "RAIN: 0%", color = if (weather.rainfall > 0) Color(0xFF00E5FF) else Color(0xFF00E676), fontSize = 9.5.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@Composable
private fun McLarenTyrePerformanceCard(
    driver: LiveTimingDriverState,
    currentLap: Int,
    subLapFraction: Float,
    modifier: Modifier = Modifier
) {
    val tyreTele = TelemetryPhysicsEngine.calculateLiveTyreTelemetry(
        compound = driver.compound,
        tyreAgeLaps = driver.tyreAge,
        currentLap = currentLap,
        subLapFraction = subLapFraction
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xCC0E1118),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(Color(0x35FFFFFF), Color(0x0CFFFFFF)))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TYRE PERFORMANCE",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Text(
                    text = tyreTele.overallHealth,
                    color = if (tyreTele.overallHealth == "OPTIMAL") Color(0xFF00E676) else if (tyreTele.overallHealth == "STABLE") TelemetryYellow else F1Red,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FL & RL
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    TyreGaugeItem(
                        label = "FL",
                        percentage = tyreTele.flWearPercent,
                        temp = "${tyreTele.flTempC}°C",
                        color = if (tyreTele.flWearPercent > 50) Color(0xFFCCFF00) else F1Red
                    )
                    TyreGaugeItem(
                        label = "RL",
                        percentage = tyreTele.rlWearPercent,
                        temp = "${tyreTele.rlTempC}°C",
                        color = if (tyreTele.rlWearPercent > 50) Color(0xFFCCFF00) else F1Red
                    )
                }

                // Top-Down F1 Car Chassis Wireframe
                Box(
                    modifier = Modifier
                        .size(width = 90.dp, height = 110.dp)
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val outlineColor = Color(0x60FFFFFF)
                        val accentColor = Color(0xA0CCFF00)

                        // Car body center
                        val bodyPath = Path().apply {
                            moveTo(w * 0.5f, h * 0.05f) // nose
                            lineTo(w * 0.65f, h * 0.22f)
                            lineTo(w * 0.70f, h * 0.55f)
                            lineTo(w * 0.80f, h * 0.80f)
                            lineTo(w * 0.75f, h * 0.95f) // rear wing
                            lineTo(w * 0.25f, h * 0.95f)
                            lineTo(w * 0.20f, h * 0.80f)
                            lineTo(w * 0.30f, h * 0.55f)
                            lineTo(w * 0.35f, h * 0.22f)
                            close()
                        }
                        drawPath(bodyPath, color = Color(0x20FFFFFF))
                        drawPath(bodyPath, color = outlineColor, style = Stroke(width = 1.4f))

                        // Cockpit
                        drawOval(
                            color = Color(0x40FFFFFF),
                            topLeft = Offset(w * 0.42f, h * 0.38f),
                            size = Size(w * 0.16f, h * 0.26f)
                        )

                        // 4 Wheels
                        val wheelW = w * 0.14f
                        val wheelH = h * 0.22f
                        drawRoundRect(accentColor, Offset(w * 0.08f, h * 0.14f), Size(wheelW, wheelH), CornerRadius(3f, 3f))
                        drawRoundRect(accentColor, Offset(w * 0.78f, h * 0.14f), Size(wheelW, wheelH), CornerRadius(3f, 3f))
                        drawRoundRect(accentColor, Offset(w * 0.05f, h * 0.68f), Size(wheelW, wheelH), CornerRadius(3f, 3f))
                        drawRoundRect(accentColor, Offset(w * 0.81f, h * 0.68f), Size(wheelW, wheelH), CornerRadius(3f, 3f))
                    }
                }

                // FR & RR
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    TyreGaugeItem(
                        label = "FR",
                        percentage = tyreTele.frWearPercent,
                        temp = "${tyreTele.frTempC}°C",
                        color = if (tyreTele.frWearPercent > 50) Color(0xFFCCFF00) else F1Red
                    )
                    TyreGaugeItem(
                        label = "RR",
                        percentage = tyreTele.rrWearPercent,
                        temp = "${tyreTele.rrTempC}°C",
                        color = if (tyreTele.rrWearPercent > 50) Color(0xFFCCFF00) else F1Red
                    )
                }
            }
        }
    }
}

@Composable
private fun TyreGaugeItem(
    label: String,
    percentage: Int,
    temp: String = "100°C",
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(label, color = Color(0xFF8E95A5), fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Text(temp, color = Color(0xFF6B7280), fontSize = 7.5.sp, fontFamily = FontFamily.Monospace)
            }
            Text("$percentage%", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
        }
        Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.Center) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 2.5f
                drawCircle(color = Color(0x25FFFFFF), style = Stroke(stroke))
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = (percentage / 100f) * 360f,
                    useCenter = false,
                    style = Stroke(stroke, cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
private fun ExpansiveCircuitMapCard(
    selectedRace: ArchiveRaceOption,
    timingDrivers: List<LiveTimingDriverState>,
    subLapFraction: Float,
    selectedDriverNumber: Int?,
    onDriverSelect: (Int) -> Unit,
    onToggleVisibility: (() -> Unit)? = null,
    carPositionsMap: Map<Int, Float>? = null,
    isCompact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val mapHeight = if (isCompact) 330.dp else 440.dp

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xCC0E1118),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(Color(0x35FFFFFF), Color(0x0CFFFFFF)))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 12.dp else 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF00E676)))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "CIRCUIT RADAR • ${selectedRace.circuitShortName}".uppercase(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "GPS RADAR TRACKER",
                        color = Color(0xFF8E95A5),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    if (onToggleVisibility != null) {
                        Spacer(Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x20FFFFFF),
                            modifier = Modifier.clickable { onToggleVisibility() }
                        ) {
                            Text(
                                text = "Hide Radar",
                                color = Color(0xFFCCCCCC),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(if (isCompact) 8.dp else 14.dp))

            // CircuitTracker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF07080C))
            ) {
                CircuitTracker(
                    circuitSlug = selectedRace.circuitSlug,
                    circuitName = "${selectedRace.name} • ${selectedRace.circuitShortName}".uppercase(),
                    drivers = timingDrivers,
                    smoothProgression = subLapFraction,
                    selectedDriverNumber = selectedDriverNumber,
                    carPositionsMap = carPositionsMap,
                    height = mapHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(if (isCompact) 8.dp else 12.dp))

            // Track Running Order Strip
            TrackRadarRunningOrderStrip(
                drivers = timingDrivers,
                selectedDriverNumber = selectedDriverNumber,
                onDriverSelect = onDriverSelect
            )
        }
    }
}

@Composable
private fun FullTimingTowerCard(
    timingDrivers: List<LiveTimingDriverState>,
    currentLap: Int,
    totalLaps: Int,
    subLapFraction: Float,
    selectedDriverNumber: Int?,
    onDriverSelect: (Int) -> Unit,
    onToggleVisibility: (() -> Unit)? = null,
    pulseAlpha: Float = 1f,
    isPlaying: Boolean = false,
    isCompact: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xCC0E1118),
        border = BorderStroke(1.dp, Brush.verticalGradient(listOf(Color(0x35FFFFFF), Color(0x0CFFFFFF)))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 12.dp else 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(F1Red))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "FIA TIMING TOWER • LAP $currentLap / $totalLaps",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "REAL-TIME DELTAS & INTERVALS",
                        color = Color(0xFF8E95A5),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    if (onToggleVisibility != null) {
                        Spacer(Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x20FFFFFF),
                            modifier = Modifier.clickable { onToggleVisibility() }
                        ) {
                            Text(
                                text = "Hide Tower",
                                color = Color(0xFFCCCCCC),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            TimingTableSectorProgressBar(
                currentLap = currentLap,
                totalLaps = totalLaps,
                subLapFraction = subLapFraction,
                isPlaying = isPlaying
            )

            Spacer(Modifier.height(10.dp))

            if (isCompact) {
                TimingTableHeader()
            } else {
                TimingTableHeaderWidescreen()
            }

            Spacer(Modifier.height(6.dp))

            // Scrollable tower content expanded to display full 20-car field comfortably
            val timingScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = if (isCompact) 360.dp else 440.dp, max = 760.dp)
                    .verticalScroll(timingScrollState),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                timingDrivers.forEach { driver ->
                    if (isCompact) {
                        TimingTableRow(
                            driver = driver,
                            subLapFraction = subLapFraction,
                            pulseAlpha = pulseAlpha,
                            isSelected = selectedDriverNumber == driver.driverNumber,
                            onClick = { onDriverSelect(driver.driverNumber) }
                        )
                    } else {
                        TimingTableRowWidescreen(
                            driver = driver,
                            subLapFraction = subLapFraction,
                            pulseAlpha = pulseAlpha,
                            isSelected = selectedDriverNumber == driver.driverNumber,
                            onClick = { onDriverSelect(driver.driverNumber) }
                        )
                    }
                    if (driver.position == 10) {
                        PointsCutoffDivider()
                    }
                }
            }
        }
    }
}

// 1. UNIFIED FLUID PIT-WALL TELEMETRY HEADER DECK
@Composable
private fun TelemetryPitWallHeader(
    selectedRace: ArchiveRaceOption,
    mode: TelemetryMode,
    isLiveActive: Boolean,
    archiveSubView: ArchiveSubView,
    isLoading: Boolean,
    raceProgressFloat: Float,
    currentLap: Int,
    totalLaps: Int,
    isPlaying: Boolean,
    replaySpeed: Int,
    isWidescreen: Boolean = false,
    layoutMode: TelemetryLayoutMode = TelemetryLayoutMode.SPLIT,
    weather: OpenF1Weather = OpenF1Weather(),
    flagText: String = "GREEN FLAG",
    flagColor: Color = TelemetryGreen,
    onOpenRaceSelector: () -> Unit,
    onModeChange: (TelemetryMode) -> Unit,
    onSubViewChange: (ArchiveSubView) -> Unit,
    onLayoutModeChange: (TelemetryLayoutMode) -> Unit = {},
    onProgressChange: (Float) -> Unit,
    onPlayPauseToggle: () -> Unit,
    onSpeedCycle: () -> Unit,
    onStepLaps: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF161822),
                        Color(0xFF0C0D13),
                        PitchBlack
                    )
                )
            )
            .padding(top = 8.dp, bottom = 6.dp)
    ) {
        if (isWidescreen) {
            // WIDESCREEN DESKTOP HEADER: Single aerodynamic command deck with integrated controls & weather telemetry
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Interactive Grand Prix Brand Identity
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenRaceSelector() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0x1EFFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(selectedRace.country),
                            contentDescription = selectedRace.country,
                            modifier = Modifier
                                .height(18.dp)
                                .width(28.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedRace.name.replace("Grand Prix", "GP").uppercase(),
                                color = MonoWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Race",
                                tint = F1Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${selectedRace.circuitShortName} • R${selectedRace.round} • ${selectedRace.totalLaps} LAPS".uppercase(),
                            color = MonoSilver.copy(alpha = 0.65f),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // 2. Playback Control Deck (Center, Expanded on Desktop)
                if (mode == TelemetryMode.ARCHIVE && !isLoading) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onStepLaps(-1) }, modifier = Modifier.size(26.dp)) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = "Step -1L", tint = MonoSilver, modifier = Modifier.size(15.dp))
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(F1Red)
                                .clickable { onPlayPauseToggle() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = MonoWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(onClick = { onStepLaps(1) }, modifier = Modifier.size(26.dp)) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Step +1L", tint = MonoSilver, modifier = Modifier.size(15.dp))
                        }

                        Spacer(Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x18FFD700))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            val formattedLap = currentLap.toString().padStart(2, '0')
                            Text(
                                text = "LAP $formattedLap/$totalLaps",
                                color = Color(0xFFFFD700),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        Slider(
                            value = raceProgressFloat,
                            onValueChange = onProgressChange,
                            valueRange = 0f..(totalLaps.toFloat() - 1f),
                            colors = SliderDefaults.colors(
                                thumbColor = F1Red,
                                activeTrackColor = F1Red,
                                inactiveTrackColor = Color(0x22FFFFFF)
                            ),
                            modifier = Modifier.weight(1f).height(20.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (replaySpeed > 1) F1Red else Color(0x1AFFFFFF))
                                .clickable { onSpeedCycle() }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${replaySpeed}X",
                                color = MonoWhite,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1f))
                }

                // 3. Right Status & Mode Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x14FFFFFF))
                            .border(BorderStroke(0.5.dp, Color(0x22FFFFFF)), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(flagColor))
                        Spacer(Modifier.width(5.dp))
                        Text("${weather.trackTemperature.toInt()}°C TRACK • ${weather.airTemperature.toInt()}°C AIR • $flagText", color = MonoSilver, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0x18FFFFFF))
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val liveActive = mode == TelemetryMode.LIVE
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (liveActive) (if (isLiveActive) TelemetryGreen.copy(alpha = 0.25f) else F1Red.copy(alpha = 0.25f)) else Color.Transparent)
                                .clickable { onModeChange(TelemetryMode.LIVE) }
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(if (isLiveActive) TelemetryGreen else if (liveActive) F1Red else MonoMuted)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "LIVE",
                                    color = if (liveActive) (if (isLiveActive) TelemetryGreen else F1Red) else MonoSilver.copy(alpha = 0.6f),
                                    fontSize = 9.sp,
                                    fontWeight = if (liveActive) FontWeight.Black else FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        val archiveActive = mode == TelemetryMode.ARCHIVE
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (archiveActive) F1Red else Color.Transparent)
                                .clickable { onModeChange(TelemetryMode.ARCHIVE) }
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = if (archiveActive) MonoWhite else MonoSilver.copy(alpha = 0.6f),
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "ARCHIVE",
                                    color = if (archiveActive) MonoWhite else MonoSilver.copy(alpha = 0.6f),
                                    fontSize = 9.sp,
                                    fontWeight = if (archiveActive) FontWeight.Black else FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            // Desktop Layout Mode Selector Deck
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PIT WALL WORKSPACE VIEW",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MonoMuted,
                    letterSpacing = 1.sp
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ObsidianVoid,
                    border = BorderStroke(1.dp, HairlineBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // SPLIT VIEW
                        val isSplit = layoutMode == TelemetryLayoutMode.SPLIT
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSplit) F1RedSubtle else Color.Transparent,
                            border = BorderStroke(1.dp, if (isSplit) F1Red.copy(alpha = 0.5f) else Color.Transparent),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onLayoutModeChange(TelemetryLayoutMode.SPLIT) }
                        ) {
                            Text(
                                text = "SPLIT VIEW",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 9.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isSplit) FontWeight.Black else FontWeight.Bold,
                                color = if (isSplit) MonoWhite else MonoMuted
                            )
                        }

                        // FULL TIMING TABLE (PRIORITY)
                        val isTiming = layoutMode == TelemetryLayoutMode.EXPAND_TIMING
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isTiming) F1RedSubtle else Color.Transparent,
                            border = BorderStroke(1.dp, if (isTiming) F1Red.copy(alpha = 0.5f) else Color.Transparent),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onLayoutModeChange(TelemetryLayoutMode.EXPAND_TIMING) }
                        ) {
                            Text(
                                text = "FULL TIMING TABLE (PRIORITY)",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 9.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isTiming) FontWeight.Black else FontWeight.Bold,
                                color = if (isTiming) MonoWhite else MonoMuted
                            )
                        }

                        // FULL CIRCUIT RADAR
                        val isCircuit = layoutMode == TelemetryLayoutMode.EXPAND_CIRCUIT
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCircuit) F1RedSubtle else Color.Transparent,
                            border = BorderStroke(1.dp, if (isCircuit) F1Red.copy(alpha = 0.5f) else Color.Transparent),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onLayoutModeChange(TelemetryLayoutMode.EXPAND_CIRCUIT) }
                        ) {
                            Text(
                                text = "FULL CIRCUIT RADAR",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 9.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isCircuit) FontWeight.Black else FontWeight.Bold,
                                color = if (isCircuit) MonoWhite else MonoMuted
                            )
                        }
                    }
                }
            }
        } else {
            // ROW 1: Fluid Grand Prix Brand & Mode Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interactive Grand Prix Brand Identity
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenRaceSelector() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0x1EFFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(selectedRace.country),
                            contentDescription = selectedRace.country,
                            modifier = Modifier
                                .height(18.dp)
                                .width(28.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedRace.name.replace("Grand Prix", "GP").uppercase(),
                                color = MonoWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Race",
                                tint = F1Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${selectedRace.circuitShortName} • R${selectedRace.round} • ${selectedRace.totalLaps} LAPS".uppercase(),
                            color = MonoSilver.copy(alpha = 0.65f),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Fluid Capsule Mode Switcher (LIVE vs ARCHIVE)
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x18FFFFFF))
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val liveActive = mode == TelemetryMode.LIVE
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (liveActive) (if (isLiveActive) TelemetryGreen.copy(alpha = 0.25f) else F1Red.copy(alpha = 0.25f)) else Color.Transparent)
                        .clickable { onModeChange(TelemetryMode.LIVE) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (isLiveActive) TelemetryGreen else if (liveActive) F1Red else MonoMuted)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "LIVE",
                            color = if (liveActive) (if (isLiveActive) TelemetryGreen else F1Red) else MonoSilver.copy(alpha = 0.6f),
                            fontSize = 9.5.sp,
                            fontWeight = if (liveActive) FontWeight.Black else FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                val archiveActive = mode == TelemetryMode.ARCHIVE
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (archiveActive) F1Red else Color.Transparent)
                        .clickable { onModeChange(TelemetryMode.ARCHIVE) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = if (archiveActive) MonoWhite else MonoSilver.copy(alpha = 0.6f),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "ARCHIVE",
                            color = if (archiveActive) MonoWhite else MonoSilver.copy(alpha = 0.6f),
                            fontSize = 9.5.sp,
                            fontWeight = if (archiveActive) FontWeight.Black else FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        if (mode == TelemetryMode.ARCHIVE && !isLoading) {
            Spacer(Modifier.height(8.dp))

            // ROW 2: View Switcher (Desktop 3-way Capsule vs Mobile 2-way Capsule)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(Color(0x14FFFFFF))
                            .padding(3.dp)
                    ) {
                        val isTower = archiveSubView == ArchiveSubView.TIMING_TABLE
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(if (isTower) Color(0xFF222634) else Color.Transparent)
                                .clickable { onSubViewChange(ArchiveSubView.TIMING_TABLE) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Leaderboard,
                                    contentDescription = null,
                                    tint = if (isTower) F1Red else MonoSilver.copy(alpha = 0.5f),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "TIMING TOWER",
                                    color = if (isTower) MonoWhite else MonoSilver.copy(alpha = 0.5f),
                                    fontSize = 10.sp,
                                    fontWeight = if (isTower) FontWeight.Black else FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        val isRadar = archiveSubView == ArchiveSubView.CIRCUIT_VIEW
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(if (isRadar) Color(0xFF222634) else Color.Transparent)
                                .clickable { onSubViewChange(ArchiveSubView.CIRCUIT_VIEW) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = if (isRadar) F1Red else MonoSilver.copy(alpha = 0.5f),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "CIRCUIT RADAR",
                                    color = if (isRadar) MonoWhite else MonoSilver.copy(alpha = 0.5f),
                                    fontSize = 10.sp,
                                    fontWeight = if (isRadar) FontWeight.Black else FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }

            Spacer(Modifier.height(6.dp))

            // ROW 3: Fluid Minimalist Playback Control Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Step -1L
                IconButton(
                    onClick = { onStepLaps(-1) },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Step -1L",
                        tint = MonoSilver,
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Play / Pause Circle Action Button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(F1Red)
                        .clickable { onPlayPauseToggle() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = MonoWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Step +1L
                IconButton(
                    onClick = { onStepLaps(1) },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Step +1L",
                        tint = MonoSilver,
                        modifier = Modifier.size(15.dp)
                    )
                }

                Spacer(Modifier.width(6.dp))

                // Lap Counter Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x18FFD700))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    val formattedLap = currentLap.toString().padStart(2, '0')
                    Text(
                        text = "LAP $formattedLap/$totalLaps",
                        color = Color(0xFFFFD700),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(Modifier.width(6.dp))

                // Scrubber Slider
                Slider(
                    value = raceProgressFloat,
                    onValueChange = onProgressChange,
                    valueRange = 0f..(totalLaps.toFloat() - 1f),
                    colors = SliderDefaults.colors(
                        thumbColor = F1Red,
                        activeTrackColor = F1Red,
                        inactiveTrackColor = Color(0x22FFFFFF)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                )

                Spacer(Modifier.width(6.dp))

                // Speed Multiplier Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (replaySpeed > 1) F1Red else Color(0x1AFFFFFF))
                        .clickable { onSpeedCycle() }
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${replaySpeed}X",
                        color = MonoWhite,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
}

// 2. TIMING TABLE SECTOR PROGRESS BAR (S1 -> S2 -> S3 Broadcast Sector Bar)
@Composable
private fun TimingTableSectorProgressBar(
    currentLap: Int,
    totalLaps: Int,
    subLapFraction: Float,
    isPlaying: Boolean
) {
    val s1Progress = (subLapFraction / 0.333f).coerceIn(0f, 1f)
    val s2Progress = ((subLapFraction - 0.333f) / 0.333f).coerceIn(0f, 1f)
    val s3Progress = ((subLapFraction - 0.666f) / 0.334f).coerceIn(0f, 1f)

    val activeSector = when {
        subLapFraction < 0.333f -> 1
        subLapFraction < 0.666f -> 2
        else -> 3
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF0F1118),
        border = BorderStroke(1.dp, Color(0x1FFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // Header Row: Lap title and Sector status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) TelemetryGreen else TelemetryYellow)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "LAP $currentLap / $totalLaps PROGRESS",
                        color = MonoWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusText = when (activeSector) {
                        1 -> "SECTOR 1 ACTIVE"
                        2 -> "SECTOR 2 ACTIVE"
                        else -> "SECTOR 3 ACTIVE"
                    }
                    val statusColor = when (activeSector) {
                        1 -> TelemetryYellow
                        2 -> TelemetryGreen
                        else -> TelemetryPurple
                    }
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${(subLapFraction * 100).toInt()}%",
                        color = MonoSilver,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(Modifier.height(5.dp))

            // 3-Part Sector Progression Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectorProgressBarSegment(
                    label = "S1",
                    progress = s1Progress,
                    isDone = subLapFraction >= 0.333f,
                    isActive = activeSector == 1,
                    modifier = Modifier.weight(1f)
                )

                SectorProgressBarSegment(
                    label = "S2",
                    progress = s2Progress,
                    isDone = subLapFraction >= 0.666f,
                    isActive = activeSector == 2,
                    modifier = Modifier.weight(1f)
                )

                SectorProgressBarSegment(
                    label = "S3",
                    progress = s3Progress,
                    isDone = subLapFraction >= 0.999f,
                    isActive = activeSector == 3,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SectorProgressBarSegment(
    label: String,
    progress: Float,
    isDone: Boolean,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = when {
                    isDone -> TelemetryGreen
                    isActive -> TelemetryYellow
                    else -> MonoMuted
                }
            )
            Text(
                text = when {
                    isDone -> "DONE"
                    isActive -> "${(progress * 100).toInt()}%"
                    else -> "WAIT"
                },
                fontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = when {
                    isDone -> TelemetryGreen
                    isActive -> TelemetryYellow
                    else -> Color(0x33FFFFFF)
                }
            )
        }

        Spacer(Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0x1AFFFFFF))
        ) {
            if (progress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progress)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (isDone) {
                                Brush.horizontalGradient(listOf(TelemetryGreen, TelemetryGreen))
                            } else {
                                Brush.horizontalGradient(listOf(F1Red, TelemetryYellow))
                            }
                        )
                )
            }
        }
    }
}

// 3. TIMING TABLE HEADER (Exact Weights, 0 Horizontal Scroll)
@Composable
private fun TimingTableHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        color = ObsidianSurface,
        border = BorderStroke(0.5.dp, Color(0x15FFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("POS", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.8f))
            Text("DRIVER & TYRE", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(2.4f))
            Text("GAP / INT", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.End, modifier = Modifier.weight(1.5f))
            Text("LAST LAP / SECTORS", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(1.6f))
            Text("STATUS", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
        }
    }
}

// Helper to get short uppercase team abbreviation
private fun getTeamAbbreviation(teamName: String): String =
    JolpicaNetworkService.getTeamAbbreviation(teamName)

// 4. TIMING TABLE ROW (Broadcast Precision with Sector Time Reveal and Position Change)
@Composable
private fun TimingTableRow(
    driver: LiveTimingDriverState,
    subLapFraction: Float,
    pulseAlpha: Float = 1.0f,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val rowBg by animateColorAsState(
        targetValue = when {
            isSelected -> Color(0x28E10600)
            driver.position == 1 -> Color(0x10FFD700)
            driver.position % 2 == 0 -> PitchBlack
            else -> ObsidianSurface.copy(alpha = 0.5f)
        },
        animationSpec = tween(120)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            color = rowBg,
            border = BorderStroke(0.5.dp, if (isSelected) F1Red else Color(0x0CFFFFFF))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. POS (0.8f) with Position Change Gain/Loss Indicator
                val posColor = when (driver.position) {
                    1 -> Color(0xFFFFD700)
                    2 -> Color(0xFFD0D0D0)
                    3 -> Color(0xFFCD7F32)
                    in 4..10 -> MonoWhite
                    else -> MonoMuted
                }
                Row(
                    modifier = Modifier.weight(0.8f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = driver.position.toString().padStart(2, '0'),
                        color = posColor,
                        fontSize = 12.sp,
                        fontWeight = if (driver.position <= 10) FontWeight.Black else FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(Modifier.width(2.dp))
                    when {
                        driver.positionChange > 0 -> {
                            Text(
                                text = "+${driver.positionChange}",
                                color = TelemetryGreen,
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        driver.positionChange < 0 -> {
                            Text(
                                text = "-${-driver.positionChange}",
                                color = F1Red,
                                fontSize = 7.5.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        else -> {
                            Text(
                                text = "-",
                                color = Color(0x28FFFFFF),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // 2. DRIVER & TYRE (2.4f)
                Row(
                    modifier = Modifier.weight(2.4f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(driver.teamColor)
                    )
                    Spacer(Modifier.width(6.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = driver.code,
                                color = MonoWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "#${driver.driverNumber}",
                                color = MonoMuted,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PirelliTyreDotBadge(compound = driver.compound, tyreAge = driver.tyreAge)
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = getTeamAbbreviation(driver.teamName),
                                color = MonoMuted,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // 3. GAP / INTERVAL (1.5f)
                Column(
                    modifier = Modifier.weight(1.5f),
                    horizontalAlignment = Alignment.End
                ) {
                    val gapColor = when {
                        driver.position == 1 -> Color(0xFFFFD700)
                        driver.gapToLeader.contains("LAP") -> Color(0xFFFFB800)
                        driver.isRetired -> F1Red
                        else -> MonoWhite
                    }

                    Text(
                        text = driver.gapToLeader,
                        color = gapColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.End
                    )
                    if (driver.position > 1 && !driver.isRetired && !driver.gapToLeader.contains("LAP")) {
                        Text(
                            text = driver.intervalAhead,
                            color = MonoMuted,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.End
                        )
                    }
                }

                // 4. LAST LAP & SECTORS (1.6f) - Sectors update as subLapFraction advances
                Column(
                    modifier = Modifier.weight(1.6f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (driver.isRetired) "RETIRED" else driver.lastLapTime.ifBlank { "--:--.---" },
                        color = if (driver.isRetired) MonoMuted else MonoWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                    if (!driver.isRetired) {
                        Spacer(Modifier.height(2.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SectorMicroPill(
                                status = if (subLapFraction >= 0.333f) driver.s1Status else SectorStatus.NONE,
                                isActive = subLapFraction < 0.333f,
                                pulseAlpha = pulseAlpha
                            )
                            SectorMicroPill(
                                status = if (subLapFraction >= 0.666f) driver.s2Status else SectorStatus.NONE,
                                isActive = subLapFraction in 0.333f..0.666f,
                                pulseAlpha = pulseAlpha
                            )
                            SectorMicroPill(
                                status = if (subLapFraction >= 0.999f) driver.s3Status else SectorStatus.NONE,
                                isActive = subLapFraction >= 0.666f,
                                pulseAlpha = pulseAlpha
                            )
                        }
                    }
                }

                // 5. STATUS (0.7f)
                Box(
                    modifier = Modifier.weight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        driver.isRetired -> {
                            StatusPill(text = "DNF", bgColor = Color(0x33E10600), textColor = F1Red)
                        }
                        driver.inPit -> {
                            StatusPill(text = "PIT", bgColor = F1Red, textColor = MonoWhite)
                        }
                        driver.isOutLap -> {
                            StatusPill(text = "OUT", bgColor = TelemetryGreen, textColor = PitchBlack)
                        }
                        driver.driverNumber == 12 -> {
                            StatusPill(text = "FL", bgColor = TelemetryPurple, textColor = MonoWhite)
                        }
                    }
                }
            }
        }

        // Expandable Telemetry Drawer
        AnimatedVisibility(visible = isSelected) {
            TelemetryDrawerRow(driver = driver, subLapFraction = subLapFraction)
        }
    }
}

// 5. WIDESCREEN TIMING TABLE HEADER (Dedicated Broadcast Columns)
@Composable
private fun TimingTableHeaderWidescreen() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        color = ObsidianSurface,
        border = BorderStroke(0.5.dp, Color(0x18FFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("POS", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(0.6f))
            Text("NO", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(0.5f))
            Text("DRIVER", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1.1f))
            Text("GAP", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.End, modifier = Modifier.weight(1.1f))
            Text("INT", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.End, modifier = Modifier.weight(1.0f))
            Text("SECTOR 1", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(1.2f))
            Text("SECTOR 2", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(1.2f))
            Text("SECTOR 3", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(1.2f))
            Text("LAST LAP", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
            Text("BEST LAP", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.End, modifier = Modifier.weight(1.2f))
            Text("TYRE / STINT", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(1.4f))
            Text("STATUS", color = MonoMuted, fontSize = 9.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center, modifier = Modifier.weight(0.9f))
        }
    }
}

// 6. WIDESCREEN TIMING TABLE ROW (Full Motorsport Intelligence Grid)
@Composable
private fun TimingTableRowWidescreen(
    driver: LiveTimingDriverState,
    subLapFraction: Float,
    pulseAlpha: Float = 1.0f,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val rowBg by animateColorAsState(
        targetValue = when {
            isSelected -> Color(0x28E10600)
            driver.position == 1 -> Color(0x10FFD700)
            driver.position % 2 == 0 -> PitchBlack
            else -> ObsidianSurface.copy(alpha = 0.5f)
        },
        animationSpec = tween(120)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            color = rowBg,
            border = BorderStroke(0.5.dp, if (isSelected) F1Red else Color(0x0CFFFFFF))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. POS (0.6f)
                val posColor = when (driver.position) {
                    1 -> Color(0xFFFFD700)
                    2 -> Color(0xFFD0D0D0)
                    3 -> Color(0xFFCD7F32)
                    in 4..10 -> MonoWhite
                    else -> MonoMuted
                }
                Row(
                    modifier = Modifier.weight(0.6f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = driver.position.toString().padStart(2, '0'),
                        color = posColor,
                        fontSize = 12.sp,
                        fontWeight = if (driver.position <= 10) FontWeight.Black else FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(Modifier.width(2.dp))
                    when {
                        driver.positionChange > 0 -> Text("+${driver.positionChange}", color = TelemetryGreen, fontSize = 7.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        driver.positionChange < 0 -> Text("-${-driver.positionChange}", color = F1Red, fontSize = 7.5.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        else -> Text("-", color = Color(0x28FFFFFF), fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                // 2. NO (0.5f)
                Text(
                    text = "#${driver.driverNumber}",
                    color = MonoMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.5f)
                )

                // 3. DRIVER (1.1f) - Clean Code + Team Tag (Full name removed)
                Row(
                    modifier = Modifier.weight(1.1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(driver.teamColor)
                    )
                    Spacer(Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(driver.teamColor.copy(alpha = 0.25f))
                            .border(0.5.dp, driver.teamColor.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = driver.code.take(2),
                            color = Color.White,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        FormulaTrackrImage(
                            url = getDriverHeadshotUrl(driver.code),
                            contentDescription = driver.code,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(Modifier.width(6.dp))

                    Column {
                        Text(
                            text = driver.code,
                            color = MonoWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = getTeamAbbreviation(driver.teamName),
                            color = MonoMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1
                        )
                    }
                }

                // 4. GAP (1.0f)
                val gapColor = when {
                    driver.position == 1 -> Color(0xFFFFD700)
                    driver.gapToLeader.contains("LAP") -> Color(0xFFFFB800)
                    driver.isRetired -> F1Red
                    else -> MonoWhite
                }
                Text(
                    text = driver.gapToLeader,
                    color = gapColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1.0f)
                )

                // 5. INT (0.9f)
                Text(
                    text = if (driver.position == 1 || driver.isRetired) "--" else driver.intervalAhead,
                    color = MonoSilver.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.9f)
                )

                // 6. SECTOR 1 (1.1f)
                val s1Active = subLapFraction < 0.333f
                val s1Done = subLapFraction >= 0.333f
                val s1Color = if (s1Done) {
                    when (driver.s1Status) {
                        SectorStatus.OVERALL_FASTEST -> TelemetryPurple
                        SectorStatus.PERSONAL_BEST -> TelemetryGreen
                        else -> MonoWhite
                    }
                } else if (s1Active) TelemetryYellow else MonoMuted
                Row(
                    modifier = Modifier.weight(1.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(s1Color.copy(alpha = if (s1Active) pulseAlpha else 1.0f))
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (s1Done) driver.s1Time else if (s1Active) "RUN" else "--",
                        color = s1Color,
                        fontSize = 10.5.sp,
                        fontWeight = if (s1Done && driver.s1Status != SectorStatus.NORMAL) FontWeight.Black else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 7. SECTOR 2 (1.1f)
                val s2Active = subLapFraction in 0.333f..0.666f
                val s2Done = subLapFraction >= 0.666f
                val s2Color = if (s2Done) {
                    when (driver.s2Status) {
                        SectorStatus.OVERALL_FASTEST -> TelemetryPurple
                        SectorStatus.PERSONAL_BEST -> TelemetryGreen
                        else -> MonoWhite
                    }
                } else if (s2Active) TelemetryYellow else MonoMuted
                Row(
                    modifier = Modifier.weight(1.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(s2Color.copy(alpha = if (s2Active) pulseAlpha else 1.0f))
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (s2Done) driver.s2Time else if (s2Active) "RUN" else "--",
                        color = s2Color,
                        fontSize = 10.5.sp,
                        fontWeight = if (s2Done && driver.s2Status != SectorStatus.NORMAL) FontWeight.Black else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 8. SECTOR 3 (1.1f)
                val s3Active = subLapFraction >= 0.666f
                val s3Done = subLapFraction >= 0.999f
                val s3Color = if (s3Done) {
                    when (driver.s3Status) {
                        SectorStatus.OVERALL_FASTEST -> TelemetryPurple
                        SectorStatus.PERSONAL_BEST -> TelemetryGreen
                        else -> MonoWhite
                    }
                } else if (s3Active) TelemetryYellow else MonoMuted
                Row(
                    modifier = Modifier.weight(1.1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(s3Color.copy(alpha = if (s3Active) pulseAlpha else 1.0f))
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (s3Done) driver.s3Time else if (s3Active) "RUN" else "--",
                        color = s3Color,
                        fontSize = 10.5.sp,
                        fontWeight = if (s3Done && driver.s3Status != SectorStatus.NORMAL) FontWeight.Black else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 9. LAST LAP (1.1f)
                Text(
                    text = if (driver.isRetired) "RETIRED" else driver.lastLapTime.ifBlank { "--:--.---" },
                    color = if (driver.isRetired) MonoMuted else MonoWhite,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1.1f)
                )

                // 10. BEST LAP (1.1f)
                val isFastestLap = driver.driverNumber == 12
                Text(
                    text = if (driver.isRetired) "--" else driver.bestLapTime.ifBlank { "1:21.104" },
                    color = if (isFastestLap) TelemetryPurple else MonoSilver,
                    fontSize = 10.5.sp,
                    fontWeight = if (isFastestLap) FontWeight.Black else FontWeight.Normal,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1.1f)
                )

                // 11. TYRE / STINT (1.3f)
                Row(
                    modifier = Modifier.weight(1.3f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PirelliTyreDotBadge(compound = driver.compound, tyreAge = driver.tyreAge)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (driver.pitStops > 0) "${driver.pitStops}P" else "0P",
                        color = MonoMuted,
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 12. STATUS / SPEED (0.9f)
                Box(
                    modifier = Modifier.weight(0.9f),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        driver.isRetired -> StatusPill(text = "DNF", bgColor = Color(0x33E10600), textColor = F1Red)
                        driver.inPit -> StatusPill(text = "PIT", bgColor = F1Red, textColor = MonoWhite)
                        driver.isOutLap -> StatusPill(text = "OUT", bgColor = TelemetryGreen, textColor = PitchBlack)
                        isFastestLap -> StatusPill(text = "FL", bgColor = TelemetryPurple, textColor = MonoWhite)
                        else -> {
                            Text(
                                text = "${driver.speedTrapKmH}",
                                color = MonoSilver.copy(alpha = 0.85f),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // Expandable Deep Telemetry Drawer
        AnimatedVisibility(visible = isSelected) {
            TelemetryDrawerRow(driver = driver, subLapFraction = subLapFraction)
        }
    }
}

// TELEMETRY EXPANDED DRAWER
@Composable
private fun TelemetryDrawerRow(driver: LiveTimingDriverState, subLapFraction: Float) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = PitchBlack,
        border = BorderStroke(0.5.dp, Color(0x22FFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${driver.code} (${getTeamAbbreviation(driver.teamName)})",
                    color = MonoWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Compound: ${driver.compound} (${driver.tyreAge} Laps)",
                    color = MonoMuted,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val s1Display = if (subLapFraction >= 0.333f) "${driver.s1Time}s" else "LIVE..."
                val s1Stat = if (subLapFraction >= 0.333f) driver.s1Status else SectorStatus.NORMAL
                MicroStatChip("S1", s1Display, s1Stat)

                val s2Display = when {
                    subLapFraction >= 0.666f -> "${driver.s2Time}s"
                    subLapFraction >= 0.333f -> "LIVE..."
                    else -> "--"
                }
                val s2Stat = if (subLapFraction >= 0.666f) driver.s2Status else SectorStatus.NONE
                MicroStatChip("S2", s2Display, s2Stat)

                val s3Display = when {
                    subLapFraction >= 0.999f -> "${driver.s3Time}s"
                    subLapFraction >= 0.666f -> "LIVE..."
                    else -> "--"
                }
                val s3Stat = if (subLapFraction >= 0.999f) driver.s3Status else SectorStatus.NONE
                MicroStatChip("S3", s3Display, s3Stat)
            }
        }
    }
}

@Composable
private fun MicroStatChip(label: String, value: String, status: SectorStatus) {
    val color = when (status) {
        SectorStatus.OVERALL_FASTEST -> TelemetryPurple
        SectorStatus.PERSONAL_BEST -> TelemetryGreen
        SectorStatus.NORMAL -> TelemetryYellow
        SectorStatus.NONE -> MonoSilver
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(ObsidianSurface)
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label: ", color = MonoMuted, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
        Text(text = value, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

// PIRELLI COMPACT BADGE
@Composable
private fun PirelliTyreDotBadge(compound: String, tyreAge: Int) {
    val (tyreColor, tyreLetter, textColor) = when (compound.uppercase()) {
        "SOFT" -> Triple(TyreSoft, "S", MonoWhite)
        "MEDIUM" -> Triple(TyreMedium, "M", PitchBlack)
        "HARD" -> Triple(TyreHard, "H", PitchBlack)
        "INTERMEDIATE" -> Triple(TyreIntermediate, "I", PitchBlack)
        "WET" -> Triple(TyreWet, "W", MonoWhite)
        else -> Triple(MonoSilver, "M", PitchBlack)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(tyreColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tyreLetter,
                color = textColor,
                fontSize = 7.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(Modifier.width(3.dp))
        Text(
            text = "${tyreAge}L",
            color = MonoSilver,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

// SECTOR MICRO PILL (Live Active Pulsing / Completed Status Color)
@Composable
private fun SectorMicroPill(
    status: SectorStatus,
    isActive: Boolean = false,
    pulseAlpha: Float = 1.0f
) {
    val color = when {
        isActive -> TelemetryYellow.copy(alpha = pulseAlpha)
        status == SectorStatus.OVERALL_FASTEST -> TelemetryPurple
        status == SectorStatus.PERSONAL_BEST -> TelemetryGreen
        status == SectorStatus.NORMAL -> TelemetryYellow
        else -> Color(0x28FFFFFF)
    }
    Box(
        modifier = Modifier
            .width(12.dp)
            .height(3.5.dp)
            .clip(RoundedCornerShape(1.5.dp))
            .background(color)
    )
}

// STATUS PILL
@Composable
private fun StatusPill(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
        )
    }
}

// POINTS CUTOFF DIVIDER
@Composable
private fun PointsCutoffDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PitchBlack)
            .padding(horizontal = 10.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0x2200D7B6)))
        Spacer(Modifier.width(6.dp))
        Text(
            text = "POINTS CUTOFF (TOP 10)",
            color = TelemetryGreen,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.width(6.dp))
        Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0x2200D7B6)))
    }
}

// TRACK RADAR MINI STRIP
@Composable
private fun TrackRadarRunningOrderStrip(
    drivers: List<LiveTimingDriverState>,
    selectedDriverNumber: Int?,
    onDriverSelect: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ObsidianSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "RUNNING ORDER • TAP TO TRACK",
                color = MonoMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(Modifier.height(6.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(drivers.take(15), key = { it.driverNumber }) { d ->
                    val isSelected = selectedDriverNumber == d.driverNumber
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color(0x33E10600) else PitchBlack)
                            .border(BorderStroke(1.dp, if (isSelected) F1Red else Color(0x18FFFFFF)), RoundedCornerShape(6.dp))
                            .clickable { onDriverSelect(d.driverNumber) }
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "P${d.position}",
                            color = if (d.position == 1) Color(0xFFFFD700) else MonoWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .width(2.5.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(d.teamColor)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = d.code,
                            color = MonoWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

// DRIVER TELEMETRY LIVE INSPECTOR CARD (F1 PIT WALL TELEMETRY HUD)
@Composable
private fun DriverTelemetryInspectorCard(
    driver: LiveTimingDriverState?,
    subLapFraction: Float,
    pulseAlpha: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    if (driver == null) return

    Surface(
        modifier = modifier,
        color = ObsidianSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header Row: P[x], Team color stripe, Name, Team, Number badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(driver.teamColor)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "P${driver.position}",
                                color = if (driver.position == 1) Color(0xFFFFD700) else MonoWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = driver.fullName.uppercase(),
                                color = MonoWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = "${getTeamAbbreviation(driver.teamName)} #${driver.driverNumber}".uppercase(),
                            color = MonoMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Driver headshot badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(driver.teamColor.copy(alpha = 0.35f))
                        .border(1.dp, driver.teamColor.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = driver.code.take(2),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    FormulaTrackrImage(
                        url = getDriverHeadshotUrl(driver.code),
                        contentDescription = driver.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Dynamic telemetry data computed from subLapFraction via smooth continuous physics
            val tele = TelemetryPhysicsEngine.calculateLiveTelemetry(subLapFraction)
            val simulatedSpeed = tele.speedKmh
            val gear = tele.gear
            val throttlePercent = tele.throttlePercent

            // Telemetry Gauges Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TelemetryGaugePill(
                    label = "SPEED",
                    primaryValue = "$simulatedSpeed",
                    secondaryValue = "KM/H",
                    accentColor = TelemetryGreen,
                    modifier = Modifier.weight(1f)
                )
                TelemetryGaugePill(
                    label = "GEAR",
                    primaryValue = "$gear",
                    secondaryValue = "DRS ${if (simulatedSpeed > 290) "ON" else "OFF"}",
                    accentColor = if (simulatedSpeed > 290) TelemetryGreen else MonoSilver,
                    modifier = Modifier.weight(1f)
                )
                TelemetryGaugePill(
                    label = "THROTTLE",
                    primaryValue = "$throttlePercent%",
                    secondaryValue = "BRAKE ${if (throttlePercent < 50) "60%" else "0%"}",
                    accentColor = if (throttlePercent == 100) TelemetryGreen else TelemetryYellow,
                    modifier = Modifier.weight(1f)
                )
                TelemetryGaugePill(
                    label = "TYRE",
                    primaryValue = driver.compound.take(4).uppercase(),
                    secondaryValue = "${driver.tyreAge} LAPS",
                    accentColor = when (driver.compound.uppercase()) {
                        "SOFT" -> TyreSoft
                        "MEDIUM" -> TyreMedium
                        "HARD" -> TyreHard
                        "INTERMEDIATE" -> TyreIntermediate
                        "WET" -> TyreWet
                        else -> MonoWhite
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(6.dp))

            // Sector status splits bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(PitchBlack)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SECTOR SPLITS",
                    color = MonoMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    val s1Color = when {
                        subLapFraction < 0.333f -> TelemetryYellow.copy(alpha = pulseAlpha)
                        driver.s1Status == SectorStatus.OVERALL_FASTEST -> TelemetryPurple
                        driver.s1Status == SectorStatus.PERSONAL_BEST -> TelemetryGreen
                        else -> TelemetryYellow
                    }
                    Text(
                        text = "S1: ${if (subLapFraction >= 0.333f) driver.s1Time + "s" else "LIVE"}",
                        color = s1Color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    val s2Color = when {
                        subLapFraction < 0.333f -> MonoMuted
                        subLapFraction < 0.666f -> TelemetryYellow.copy(alpha = pulseAlpha)
                        driver.s2Status == SectorStatus.OVERALL_FASTEST -> TelemetryPurple
                        driver.s2Status == SectorStatus.PERSONAL_BEST -> TelemetryGreen
                        else -> TelemetryYellow
                    }
                    Text(
                        text = "S2: ${if (subLapFraction >= 0.666f) driver.s2Time + "s" else if (subLapFraction >= 0.333f) "LIVE" else "--"}",
                        color = s2Color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    val s3Color = when {
                        subLapFraction < 0.666f -> MonoMuted
                        subLapFraction < 0.999f -> TelemetryYellow.copy(alpha = pulseAlpha)
                        driver.s3Status == SectorStatus.OVERALL_FASTEST -> TelemetryPurple
                        driver.s3Status == SectorStatus.PERSONAL_BEST -> TelemetryGreen
                        else -> TelemetryYellow
                    }
                    Text(
                        text = "S3: ${if (subLapFraction >= 0.999f) driver.s3Time + "s" else if (subLapFraction >= 0.666f) "LIVE" else "--"}",
                        color = s3Color,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun TelemetryGaugePill(
    label: String,
    primaryValue: String,
    secondaryValue: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = PitchBlack,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = MonoMuted,
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = primaryValue,
                color = accentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
            if (secondaryValue.isNotBlank()) {
                Text(
                    text = secondaryValue,
                    color = MonoSilver,
                    fontSize = 7.5.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}


// 5. ARCHIVE RACE SELECTOR BOTTOMSHEET
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArchiveRaceSelectorBottomSheet(
    races: List<ArchiveRaceOption>,
    selectedRace: ArchiveRaceOption,
    onSelectRace: (ArchiveRaceOption) -> Unit,
    onDismiss: () -> Unit
) {
    var sortDescending by remember { mutableStateOf(true) }
    val sortedRaces = remember(races, sortDescending) {
        if (sortDescending) races.sortedByDescending { it.round }
        else races.sortedBy { it.round }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ObsidianSurfaceElevated,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color(0x60FFFFFF)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SELECT 2026 GRAND PRIX",
                        color = MonoWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "2026 CHAMPIONSHIP ROUNDS",
                        color = MonoMuted,
                        fontSize = 9.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x25FFFFFF))
                        .border(1.dp, Color(0x35FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { sortDescending = !sortDescending }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Sort Rounds",
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = if (sortDescending) "RD 14 → 1" else "RD 1 → 14",
                        color = MonoWhite,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
            ) {
                items(sortedRaces, key = { it.sessionKey }) { race ->
                    val isCurrent = race.sessionKey == selectedRace.sessionKey
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isCurrent) Color(0x28E10600) else PitchBlack)
                            .border(BorderStroke(if (isCurrent) 1.dp else 0.5.dp, if (isCurrent) F1Red else Color(0x14FFFFFF)), RoundedCornerShape(10.dp))
                            .clickable { onSelectRace(race) }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f, fill = false)) {
                            FormulaTrackrImage(
                                url = getCountryFlagUrl(race.country),
                                contentDescription = race.country,
                                modifier = Modifier
                                    .height(18.dp)
                                    .width(26.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (isCurrent) F1Red else Color(0x25FFFFFF)
                                    ) {
                                        Text(
                                            text = "RD ${race.round}",
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = MonoWhite
                                        )
                                    }
                                    Text(
                                        text = race.name.uppercase(),
                                        color = if (isCurrent) MonoWhite else MonoSilver,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "${race.circuitShortName} • ${race.country} • ${race.totalLaps} Laps • ${race.trackLengthKm}",
                                    color = MonoMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        if (isCurrent) {
                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = F1Red, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}



// NEXT ROUND TIMETABLE (When live is idle)
@Composable
private fun NextRoundTimetableView(
    nextRace: Race?,
    onSwitchToArchive: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, GlassBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(F1RedSubtle),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = F1Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "NO LIVE TIMING SESSION ACTIVE",
                    color = MonoWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Live telemetry feed is idle between grand prix sessions.",
                    color = MonoMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = onSwitchToArchive,
                    colors = ButtonDefaults.buttonColors(containerColor = F1Red),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "PLAY 2026 ARCHIVE TELEMETRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (nextRace != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Flag, contentDescription = null, tint = F1Red, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "NEXT GRAND PRIX TIMETABLE",
                    color = MonoSilver,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = GlassSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GlassBorderActive)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ROUND ${nextRace.round} • ${nextRace.raceName.uppercase()}",
                        color = MonoWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "${nextRace.circuit.circuitName}, ${nextRace.circuit.location} (${nextRace.circuit.country})",
                        color = F1Red,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(Modifier.height(12.dp))

                    val schedule = nextRace.schedule
                    val sessions = listOfNotNull(
                        schedule?.fp1?.let { Triple("PRACTICE 1 (FP1)", it.date ?: "", it.time ?: "") },
                        schedule?.fp2?.let { Triple("PRACTICE 2 (FP2)", it.date ?: "", it.time ?: "") },
                        schedule?.fp3?.let { Triple("PRACTICE 3 (FP3)", it.date ?: "", it.time ?: "") },
                        schedule?.sprintQualy?.let { Triple("SPRINT QUALIFYING", it.date ?: "", it.time ?: "") },
                        schedule?.sprintRace?.let { Triple("SPRINT RACE", it.date ?: "", it.time ?: "") },
                        schedule?.qualy?.let { Triple("QUALIFYING", it.date ?: "", it.time ?: "") },
                        schedule?.race?.let { Triple("GRAND PRIX (RACE)", it.date ?: nextRace.date, it.time ?: nextRace.time) }
                    )

                    sessions.forEachIndexed { idx, (sessionName, date, time) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (idx % 2 == 0) ObsidianSurface else PitchBlack)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = sessionName,
                                    color = if (sessionName.contains("RACE")) F1Red else MonoWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = date,
                                    color = MonoMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = time.removeSuffix("Z").ifBlank { "TBD" },
                                color = MonoSilver,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        if (idx < sessions.lastIndex) Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

// LIVE ACTIVE VIEW
@Composable
private fun LiveActiveTimingView(
    sessionName: String,
    timingDrivers: List<LiveTimingDriverState>,
    currentLap: Int,
    totalLaps: Int,
    selectedDriverNumber: Int?,
    onDriverSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TimingTableHeader()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(PitchBlack)
        ) {
            items(timingDrivers, key = { it.driverNumber }) { driver ->
                TimingTableRow(
                    driver = driver,
                    subLapFraction = 1.0f,
                    isSelected = selectedDriverNumber == driver.driverNumber,
                    onClick = { onDriverSelect(driver.driverNumber) }
                )
                if (driver.position == 10) {
                    PointsCutoffDivider()
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  RACE STATUS CONTROL STRIP (Track Flag, Weather, Race Control, Team Radio)
// ══════════════════════════════════════════════════════════════

@Composable
private fun RaceStatusControlStrip(
    raceControl: List<OpenF1RaceControl>,
    weather: OpenF1Weather,
    teamRadio: List<OpenF1TeamRadio>,
    flagText: String,
    flagColor: Color,
    currentLap: Int,
    isWidescreen: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val latestMessage = raceControl.lastOrNull()?.message ?: "TRACK CLEAR - ALL SECTORS GREEN"
    val latestRadio = teamRadio.lastOrNull()

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xD80D111A),
        border = BorderStroke(1.dp, if (flagColor != TelemetryGreen && flagColor != Color(0xFF00E5FF)) flagColor.copy(alpha = 0.6f) else Color(0x28FFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (isWidescreen) {
                    // Widescreen Single Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleExpand() }
                            .padding(horizontal = 14.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Flag Pill & Race Control Headline
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            // Flag Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = flagColor.copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, flagColor.copy(alpha = 0.8f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(flagColor)
                                    )
                                    Text(
                                        text = flagText,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = flagColor,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            // Bulletin Message
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "FIA RC:",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextMuted
                                )
                                Text(
                                    text = latestMessage.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Right: Weather Pill & Radio & Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PitchBlack.copy(alpha = 0.6f),
                                border = BorderStroke(0.5.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "AIR ${weather.airTemperature.toInt()}°C",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoSilver
                                    )
                                    Text(
                                        text = "TRACK ${weather.trackTemperature.toInt()}°C",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TelemetryYellow
                                    )
                                    Text(
                                        text = if (weather.rainfall > 0) "RAIN WET" else "DRY 0%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (weather.rainfall > 0) Color(0xFF00E5FF) else TelemetryGreen
                                    )
                                    Text(
                                        text = "HUM ${weather.humidity.toInt()}%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextMuted
                                    )
                                }
                            }

                            if (latestRadio != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0x1800E5FF),
                                    border = BorderStroke(1.dp, Color(0x6000E5FF))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "RADIO #${latestRadio.driverNumber}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF00E5FF)
                                        )
                                        Text(
                                            text = "LIVE",
                                            fontSize = 7.5.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = F1Red
                                        )
                                    }
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isExpanded) F1Red.copy(alpha = 0.2f) else PitchBlack.copy(alpha = 0.5f),
                                border = BorderStroke(0.5.dp, if (isExpanded) F1Red else GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = if (isExpanded) "CLOSE" else "INTEL",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (isExpanded) F1Red else MonoSilver
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Mobile Multi-Line Layout for Complete Text Readability
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleExpand() }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Line 1: Flag Status & Weather / Radio / Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Flag Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = flagColor.copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, flagColor.copy(alpha = 0.8f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(flagColor)
                                    )
                                    Text(
                                        text = flagText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = flagColor,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            // Right: Weather snippet & INTEL toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = PitchBlack.copy(alpha = 0.6f),
                                    border = BorderStroke(0.5.dp, GlassBorder)
                                ) {
                                    Text(
                                        text = "TRACK ${weather.trackTemperature.toInt()}°C",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TelemetryYellow,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                if (latestRadio != null) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0x1800E5FF),
                                        border = BorderStroke(1.dp, Color(0x6000E5FF))
                                    ) {
                                        Text(
                                            text = "#${latestRadio.driverNumber} RADIO",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF00E5FF),
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isExpanded) F1Red.copy(alpha = 0.2f) else PitchBlack.copy(alpha = 0.5f),
                                    border = BorderStroke(0.5.dp, if (isExpanded) F1Red else GlassBorder)
                                ) {
                                    Text(
                                        text = if (isExpanded) "CLOSE" else "INTEL",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (isExpanded) F1Red else MonoSilver,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // Line 2: Full-width FIA RC Bulletin Row with Soft Wrap
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x16FFFFFF))
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "FIA RC:",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFFF9100)
                            )
                            Text(
                                text = latestMessage.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite,
                                softWrap = true,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Expanded Drawer (Race Control bulletin feed, full Weather station, Team Radio list)
                AnimatedVisibility(visible = isExpanded) {
                    RaceStatusDetailsDrawer(
                        raceControl = raceControl,
                        weather = weather,
                        teamRadio = teamRadio,
                        isWidescreen = isWidescreen
                    )
                }
            }
        }
    }

@Composable
private fun RaceStatusDetailsDrawer(
    raceControl: List<OpenF1RaceControl>,
    weather: OpenF1Weather,
    teamRadio: List<OpenF1TeamRadio>,
    isWidescreen: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PitchBlack.copy(alpha = 0.95f))
            .border(BorderStroke(1.dp, Color(0x18FFFFFF)))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isWidescreen) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Col 1: Race Control Bulletin Feed (42%)
                Column(modifier = Modifier.weight(0.42f)) {
                    Text(
                        text = "FIA RACE CONTROL BULLETINS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        raceControl.takeLast(5).reversed().forEach { rc ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ObsidianSurface,
                                border = BorderStroke(0.5.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val catColor = when (rc.category) {
                                        "Flag" -> TelemetryYellow
                                        "SafetyCar" -> Color(0xFFFF9100)
                                        "Drs" -> TelemetryGreen
                                        else -> Color(0xFF00E5FF)
                                    }
                                    if (rc.lapNumber != null) {
                                        Text(
                                            text = "L${rc.lapNumber}",
                                            fontSize = 7.5.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = MonoMuted
                                        )
                                    }
                                    Text(
                                        text = rc.category.uppercase(),
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = catColor
                                    )
                                    Text(
                                        text = rc.message.uppercase(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Col 2: Weather Telemetry Hub (32%)
                Column(modifier = Modifier.weight(0.32f)) {
                    Text(
                        text = "PIT WALL METEOROLOGICAL STATION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ObsidianSurface,
                        border = BorderStroke(0.5.dp, GlassBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("AIR TEMP", fontSize = 8.5.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                                Text("${weather.airTemperature}°C", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MonoWhite, fontFamily = FontFamily.Monospace)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("TRACK TEMP", fontSize = 8.5.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                                Text("${weather.trackTemperature}°C", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = TelemetryYellow, fontFamily = FontFamily.Monospace)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("HUMIDITY", fontSize = 8.5.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                                Text("${weather.humidity}%", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MonoWhite, fontFamily = FontFamily.Monospace)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("PRECIPITATION", fontSize = 8.5.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                                Text(if (weather.rainfall > 0) "RAIN • WET TRACK" else "DRY • 0.0 mm", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = if (weather.rainfall > 0) Color(0xFF00E5FF) else TelemetryGreen, fontFamily = FontFamily.Monospace)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("WIND VELOCITY", fontSize = 8.5.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                                Text("${weather.windSpeed} m/s @ ${weather.windDirection}°", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MonoSilver, fontFamily = FontFamily.Monospace)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("PRESSURE", fontSize = 8.5.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                                Text("${weather.pressure} hPa", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MonoSilver, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }

                // Col 3: Team Radio Communications (26%)
                Column(modifier = Modifier.weight(0.26f)) {
                    Text(
                        text = "TEAM RADIO COMMUNICATIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        teamRadio.takeLast(4).reversed().forEach { radio ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ObsidianSurface,
                                border = BorderStroke(0.5.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color(0xFF00E5FF).copy(alpha = 0.2f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "#${radio.driverNumber}${if (radio.lapNumber != null) " L${radio.lapNumber}" else ""}",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = FontFamily.Monospace,
                                                color = Color(0xFF00E5FF)
                                            )
                                        }
                                        Text(
                                            text = if (radio.message.isNotBlank()) radio.message else "TEAM RADIO CALL",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = MonoWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Text(
                                        text = "AUDIO",
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = TelemetryGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Compact Mobile Stack
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "METEOROLOGY & RACE CONTROL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MonoWhite
                )
                Text(
                    text = "AIR ${weather.airTemperature}°C • TRACK ${weather.trackTemperature}°C • HUM ${weather.humidity}% • ${if (weather.rainfall > 0) "RAIN" else "DRY"}",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TelemetryYellow
                )
                raceControl.takeLast(3).reversed().forEach { rc ->
                    Text(
                        text = "• ${if (rc.lapNumber != null) "L${rc.lapNumber} " else ""}[${rc.category.uppercase()}] ${rc.message.uppercase()}",
                        fontSize = 8.5.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite
                    )
                }
            }
        }
    }
}


