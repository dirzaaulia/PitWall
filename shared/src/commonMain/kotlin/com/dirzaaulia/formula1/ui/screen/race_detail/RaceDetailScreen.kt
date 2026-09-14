package com.dirzaaulia.formula1.ui.screen.race_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.GrandPrixWeekendResults
import com.dirzaaulia.formula1.model.PodiumResult
import com.dirzaaulia.formula1.model.PracticeClassificationRow
import com.dirzaaulia.formula1.model.QualifyingClassificationRow
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.model.RaceClassificationRow
import com.dirzaaulia.formula1.model.SprintClassificationRow
import com.dirzaaulia.formula1.network.NetworkRepository
import com.dirzaaulia.formula1.theme.DarkCard
import com.dirzaaulia.formula1.theme.DarkCardElevated
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurface
import com.dirzaaulia.formula1.theme.HairlineBorder
import com.dirzaaulia.formula1.theme.MonoMuted
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurface
import com.dirzaaulia.formula1.theme.ObsidianSurfaceElevated
import com.dirzaaulia.formula1.theme.ObsidianVoid
import com.dirzaaulia.formula1.theme.TelemetryGreen
import com.dirzaaulia.formula1.theme.TelemetryPurple
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.theme.TextSilver
import com.dirzaaulia.formula1.ui.component.CircuitVectorFallback
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.ui.component.ShimmerCard
import com.dirzaaulia.formula1.util.CircuitSpecs
import com.dirzaaulia.formula1.util.FlagColorPalette
import com.dirzaaulia.formula1.util.LocalWindowSizeClass
import com.dirzaaulia.formula1.util.SessionCountdownScheduler
import com.dirzaaulia.formula1.util.calculateCountdownRemaining
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getCircuitMapUrl
import com.dirzaaulia.formula1.util.getCircuitSpecs
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.isRaceFinished
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay

private val GoldMedal = Color(0xFFFFD700)
private val SilverMedal = Color(0xFFC0C0C0)
private val BronzeMedal = Color(0xFFCD7F32)

enum class ResultSessionTab {
    RACE,
    QUALIFYING,
    SPRINT,
    PRACTICE
}

enum class PracticeSubTab {
    FP1,
    FP2,
    FP3
}

@Composable
fun RaceDetailScreen(
    race: Race,
    onBack: () -> Unit
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val isWidescreen = windowSizeClass.isWidescreen

    val isFinished = remember(race) {
        isRaceFinished(race)
    }

    var weekendResults by remember(race.round) { mutableStateOf<GrandPrixWeekendResults?>(null) }
    var resultsLoading by remember(race.round) { mutableStateOf(false) }

    LaunchedEffect(race.round) {
        if (isFinished) {
            resultsLoading = true
            weekendResults = NetworkRepository.getGrandPrixResults(round = race.round, isSprint = race.isSprint)
            resultsLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation Bar
        RaceDetailTopBar(
            race = race,
            isFinished = isFinished,
            onBack = onBack
        )

        if (isWidescreen) {
            // Widescreen 2-column layout
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Hero Banner + Weekend Results Center OR Countdown + Schedule
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RaceHeroAtmosphericCard(
                        race = race,
                        isFinished = isFinished,
                        isWidescreen = true
                    )

                    if (isFinished) {
                        if (resultsLoading) {
                            ShimmerCard(height = 320.dp)
                        } else if (weekendResults != null) {
                            GrandPrixResultsDeck(
                                results = weekendResults!!,
                                isSprint = race.isSprint,
                                isWidescreen = true
                            )
                        }
                    }

                    WeekendScheduleCard(race = race)
                }

                // Right Column: Circuit Radar + Technical Specs Bento Matrix
                Column(
                    modifier = Modifier
                        .weight(0.85f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircuitGeometryCard(race = race, isWidescreen = true)
                    CircuitTechnicalBentoCard(race = race)
                }
            }
        } else {
            // Mobile / Compact 1-column scrollable layout
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    RaceHeroAtmosphericCard(
                        race = race,
                        isFinished = isFinished,
                        isWidescreen = false
                    )
                }

                if (isFinished) {
                    if (resultsLoading) {
                        item { ShimmerCard(height = 300.dp) }
                    } else if (weekendResults != null) {
                        item {
                            GrandPrixResultsDeck(
                                results = weekendResults!!,
                                isSprint = race.isSprint,
                                isWidescreen = false
                            )
                        }
                    }
                }

                item { WeekendScheduleCard(race = race) }
                item { CircuitGeometryCard(race = race, isWidescreen = false) }
                item { CircuitTechnicalBentoCard(race = race) }
            }
        }
    }
}

@Composable
private fun RaceDetailTopBar(
    race: Race,
    isFinished: Boolean,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
            color = ObsidianSurfaceElevated,
            border = BorderStroke(1.dp, GlassBorderActive),
            modifier = Modifier.clickable { onBack() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MonoWhite,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "CALENDAR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MonoWhite,
                    letterSpacing = 1.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                color = if (isFinished) Color(0x22FFFFFF) else F1RedSubtle,
                border = BorderStroke(1.dp, if (isFinished) Color(0x44FFFFFF) else F1Red.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "/ ROUND ${format2Digits(race.round)} • 2026 CALENDAR /",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MonoWhite,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun RaceHeroAtmosphericCard(
    race: Race,
    isFinished: Boolean,
    isWidescreen: Boolean
) {
    val flagColor = FlagColorPalette.getCountryFlagColor(race.circuit.country)
    val accentColor = FlagColorPalette.getCountryAccentColor(race.circuit.country)

    val backgroundBrush = if (isFinished) {
        Brush.horizontalGradient(
            colors = listOf(
                flagColor.copy(alpha = 0.22f),
                Color(0xFF0D1017),
                Color(0xFF08090E)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                flagColor.copy(alpha = 0.38f),
                accentColor.copy(alpha = 0.14f),
                Color(0xFF0A0C13)
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, if (isFinished) flagColor.copy(alpha = 0.35f) else flagColor.copy(alpha = 0.7f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundBrush)
                .padding(if (isWidescreen) 24.dp else 18.dp)
        ) {
            // Country Flag Silhouette Watermark
            FormulaTrackrImage(
                url = getCountryFlagUrl(race.circuit.country),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(if (isWidescreen) 260.dp else 160.dp)
                    .graphicsLayer { alpha = if (isFinished) 0.08f else 0.16f }
                    .clip(RoundedCornerShape(16.dp))
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Badges Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                            color = if (isFinished) Color(0x22FFFFFF) else F1Red,
                            border = BorderStroke(1.dp, if (isFinished) Color(0x44FFFFFF) else F1Red)
                        ) {
                            Text(
                                text = if (isFinished) "/ FINAL RESULT /" else "/ UPCOMING GRAND PRIX /",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = TextPure,
                                letterSpacing = 1.sp
                            )
                        }

                        if (race.isSprint) {
                            Surface(
                                shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                                color = Color(0xFFFF8000),
                                border = BorderStroke(1.dp, Color(0xFFFF8000))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Sprint",
                                        tint = TextPure,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "SPRINT WEEKEND",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextPure
                                    )
                                }
                            }
                        }
                    }

                    // Host Country & Weather
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(race.circuit.country),
                            contentDescription = race.circuit.country,
                            modifier = Modifier
                                .height(18.dp)
                                .width(28.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }
                }

                // Race Title & Circuit
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = race.raceName.uppercase(),
                        fontSize = if (isWidescreen) 28.sp else 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = flagColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${race.circuit.circuitName} • ${race.circuit.location}, ${race.circuit.country}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSilver
                        )
                    }
                }

                // Telemetry Specs Row / Live Countdown
                if (!isFinished) {
                    DetailCountdownDeck(race = race)
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ObsidianVoid,
                            border = BorderStroke(1.dp, HairlineBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00D2BE), modifier = Modifier.size(14.dp))
                                Text(
                                    text = "COMPLETED // ${race.date}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF00D2BE)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ObsidianVoid,
                            border = BorderStroke(1.dp, HairlineBorder)
                        ) {
                            Text(
                                text = "${race.totalLaps} LAPS • ${race.trackLength}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MonoSilver
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailCountdownDeck(race: Race) {
    var upcomingSession by remember(race) {
        mutableStateOf(SessionCountdownScheduler.resolveNextUpcomingSession(race))
    }

    var countdown by remember(upcomingSession.targetEpochMillis) {
        mutableStateOf(calculateCountdownRemaining(upcomingSession.targetEpochMillis))
    }

    LaunchedEffect(race) {
        while (true) {
            val resolved = SessionCountdownScheduler.resolveNextUpcomingSession(race)
            if (resolved.targetEpochMillis != upcomingSession.targetEpochMillis || resolved.sessionName != upcomingSession.sessionName) {
                upcomingSession = resolved
            }
            countdown = calculateCountdownRemaining(upcomingSession.targetEpochMillis)
            delay(1000L)
        }
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = ObsidianVoid,
        border = BorderStroke(1.dp, F1Red.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(F1Red)
                )
                Text(
                    text = "NEXT: ${upcomingSession.sessionBadge}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = F1Red,
                    letterSpacing = 1.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailCountdownBlock(value = countdown.days, label = "DAYS")
                Text(":", fontSize = 18.sp, fontWeight = FontWeight.Black, color = F1Red)
                DetailCountdownBlock(value = countdown.hours, label = "HRS")
                Text(":", fontSize = 18.sp, fontWeight = FontWeight.Black, color = F1Red)
                DetailCountdownBlock(value = countdown.minutes, label = "MINS")
                Text(":", fontSize = 18.sp, fontWeight = FontWeight.Black, color = F1Red)
                DetailCountdownBlock(value = countdown.seconds, label = "SECS")
            }
        }
    }
}

@Composable
private fun DetailCountdownBlock(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = format2Digits(value),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = MonoWhite
        )
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = TextMuted,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun GrandPrixResultsDeck(
    results: GrandPrixWeekendResults,
    isSprint: Boolean,
    isWidescreen: Boolean
) {
    var selectedTab by remember { mutableStateOf(ResultSessionTab.RACE) }
    var selectedPracticeTab by remember { mutableStateOf(PracticeSubTab.FP1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header & Session Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = GoldMedal,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "/ WEEKEND RESULTS CENTER /",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0x2200D2BE),
                    border = BorderStroke(1.dp, Color(0x6600D2BE))
                ) {
                    Text(
                        text = "FULL CLASSIFICATION",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00D2BE)
                    )
                }
            }

            // Session Sub-Tabs
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ObsidianVoid,
                border = BorderStroke(1.dp, HairlineBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ResultSessionTabPill(
                        title = "/ RACE /",
                        isSelected = selectedTab == ResultSessionTab.RACE,
                        onClick = { selectedTab = ResultSessionTab.RACE },
                        modifier = Modifier.weight(1f)
                    )
                    ResultSessionTabPill(
                        title = "/ QUALI /",
                        isSelected = selectedTab == ResultSessionTab.QUALIFYING,
                        onClick = { selectedTab = ResultSessionTab.QUALIFYING },
                        modifier = Modifier.weight(1f)
                    )
                    if (isSprint) {
                        ResultSessionTabPill(
                            title = "/ SPRINT /",
                            isSelected = selectedTab == ResultSessionTab.SPRINT,
                            onClick = { selectedTab = ResultSessionTab.SPRINT },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    ResultSessionTabPill(
                        title = "/ PRACTICE /",
                        isSelected = selectedTab == ResultSessionTab.PRACTICE,
                        onClick = { selectedTab = ResultSessionTab.PRACTICE },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                ResultSessionTab.RACE -> {
                    RaceResultsClassificationView(rows = results.raceResults)
                }
                ResultSessionTab.QUALIFYING -> {
                    QualifyingResultsClassificationView(rows = results.qualifyingResults)
                }
                ResultSessionTab.SPRINT -> {
                    SprintResultsClassificationView(rows = results.sprintResults)
                }
                ResultSessionTab.PRACTICE -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Practice FP1 / FP2 / FP3 pill switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PracticeSubTabPill("FP1", selectedPracticeTab == PracticeSubTab.FP1) { selectedPracticeTab = PracticeSubTab.FP1 }
                            PracticeSubTabPill("FP2", selectedPracticeTab == PracticeSubTab.FP2) { selectedPracticeTab = PracticeSubTab.FP2 }
                            PracticeSubTabPill("FP3", selectedPracticeTab == PracticeSubTab.FP3) { selectedPracticeTab = PracticeSubTab.FP3 }
                        }

                        val rows = when (selectedPracticeTab) {
                            PracticeSubTab.FP1 -> results.fp1Results
                            PracticeSubTab.FP2 -> results.fp2Results
                            PracticeSubTab.FP3 -> results.fp3Results
                        }
                        PracticeResultsClassificationView(rows = rows)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultSessionTabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) F1Red else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) F1Red else Color.Transparent),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isSelected) MonoWhite else MonoMuted,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun PracticeSubTabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) F1RedSubtle else ObsidianVoid,
        border = BorderStroke(1.dp, if (isSelected) F1Red else HairlineBorder),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = if (isSelected) MonoWhite else MonoSilver
        )
    }
}

@Composable
private fun RaceResultsClassificationView(rows: List<RaceClassificationRow>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            val (badgeColor, posLabel) = when (row.position) {
                1 -> Pair(GoldMedal, "P1")
                2 -> Pair(SilverMedal, "P2")
                3 -> Pair(BronzeMedal, "P3")
                else -> Pair(MonoSilver, "P${format2Digits(row.position)}")
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, if (row.position <= 3) badgeColor.copy(alpha = 0.4f) else HairlineBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (row.position <= 3) badgeColor.copy(alpha = 0.2f) else ObsidianVoid,
                            border = BorderStroke(1.dp, if (row.position <= 3) badgeColor else HairlineBorder)
                        ) {
                            Text(
                                text = posLabel,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = badgeColor
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(width = 3.5.dp, height = 26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(row.teamColor)
                        )

                        Text(
                            text = row.driverCode,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )

                        Column {
                            Text(
                                text = row.driverName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonoWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = row.teamName,
                                fontSize = 10.sp,
                                color = MonoMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (row.isFastestLap) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = TelemetryPurple.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, TelemetryPurple)
                            ) {
                                Text(
                                    text = "FL",
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = TelemetryPurple
                                )
                            }
                        }

                        if (row.points > 0) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = F1RedSubtle,
                                border = BorderStroke(1.dp, F1Red.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "+${row.points}",
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                            }
                        }

                        Text(
                            text = row.timeOrGap,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (row.position == 1) GoldMedal else MonoSilver
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QualifyingResultsClassificationView(rows: List<QualifyingClassificationRow>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            val isPole = row.position == 1

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, if (isPole) GoldMedal.copy(alpha = 0.6f) else HairlineBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isPole) GoldMedal.copy(alpha = 0.2f) else ObsidianVoid,
                            border = BorderStroke(1.dp, if (isPole) GoldMedal else HairlineBorder)
                        ) {
                            Text(
                                text = if (isPole) "POLE" else "P${format2Digits(row.position)}",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = if (isPole) GoldMedal else MonoSilver
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(width = 3.5.dp, height = 26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(row.teamColor)
                        )

                        Text(
                            text = row.driverCode,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )

                        Column {
                            Text(
                                text = row.driverName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonoWhite,
                                maxLines = 1
                            )
                            Text(
                                text = "${row.q1} • ${row.q2} • ${row.q3}",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MonoMuted
                            )
                        }
                    }

                    Text(
                        text = if (isPole) row.q3.ifBlank { row.q1 } else row.gapToPole,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isPole) GoldMedal else MonoSilver
                    )
                }
            }
        }
    }
}

@Composable
private fun SprintResultsClassificationView(rows: List<SprintClassificationRow>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, HairlineBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ObsidianVoid,
                            border = BorderStroke(1.dp, HairlineBorder)
                        ) {
                            Text(
                                text = "P${format2Digits(row.position)}",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoSilver
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(width = 3.5.dp, height = 26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(row.teamColor)
                        )

                        Text(
                            text = row.driverCode,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )

                        Column {
                            Text(
                                text = row.driverName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonoWhite
                            )
                            Text(
                                text = row.teamName,
                                fontSize = 10.sp,
                                color = MonoMuted
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (row.points > 0) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFF8000).copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Color(0xFFFF8000))
                            ) {
                                Text(
                                    text = "+${row.points} PTS",
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFFF8000)
                                )
                            }
                        }

                        Text(
                            text = row.timeOrGap,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MonoWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeResultsClassificationView(rows: List<PracticeClassificationRow>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            val isLeader = row.position == 1

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ObsidianSurface,
                border = BorderStroke(1.dp, if (isLeader) TelemetryGreen.copy(alpha = 0.5f) else HairlineBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isLeader) TelemetryGreen.copy(alpha = 0.2f) else ObsidianVoid,
                            border = BorderStroke(1.dp, if (isLeader) TelemetryGreen else HairlineBorder)
                        ) {
                            Text(
                                text = "P${format2Digits(row.position)}",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = if (isLeader) TelemetryGreen else MonoSilver
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(width = 3.5.dp, height = 26.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(row.teamColor)
                        )

                        Text(
                            text = row.driverCode,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )

                        Column {
                            Text(
                                text = row.driverName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonoWhite
                            )
                            Text(
                                text = "${row.teamName} • ${row.lapsCompleted} LAPS",
                                fontSize = 10.sp,
                                color = MonoMuted
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = row.bestLap,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = if (isLeader) TelemetryGreen else MonoWhite
                        )
                        Text(
                            text = row.gapToLeader,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MonoMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekendScheduleCard(race: Race) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = F1Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "/ WEEKEND TIMETABLE & SESSIONS /",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = ObsidianVoid,
                    border = BorderStroke(1.dp, HairlineBorder)
                ) {
                    Text(
                        text = "TRACK LOCAL TIME",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MonoMuted
                    )
                }
            }

            val sessions = if (race.isSprint) {
                listOf(
                    Triple("FREE PRACTICE 1", "FP1", "12:30"),
                    Triple("SPRINT QUALIFYING", "SQ", "16:30"),
                    Triple("SPRINT RACE", "SPRINT", "11:00"),
                    Triple("QUALIFYING", "QUALI", "15:00"),
                    Triple("GRAND PRIX RACE", "RACE", "14:00")
                )
            } else {
                listOf(
                    Triple("FREE PRACTICE 1", "FP1", "13:30"),
                    Triple("FREE PRACTICE 2", "FP2", "17:00"),
                    Triple("FREE PRACTICE 3", "FP3", "12:30"),
                    Triple("QUALIFYING", "QUALI", "16:00"),
                    Triple("GRAND PRIX RACE", "RACE", "15:00")
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                sessions.forEach { (name, badge, time) ->
                    val isGrandPrix = badge == "RACE"
                    val isSprintBadge = badge == "SPRINT" || badge == "SQ"

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isGrandPrix) F1RedSubtle else ObsidianSurface,
                        border = BorderStroke(1.dp, if (isGrandPrix) F1Red.copy(alpha = 0.6f) else HairlineBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isGrandPrix) F1Red else if (isSprintBadge) Color(0xFFFF8000) else ObsidianVoid,
                                    border = BorderStroke(1.dp, if (isGrandPrix) F1Red else if (isSprintBadge) Color(0xFFFF8000) else HairlineBorder)
                                ) {
                                    Text(
                                        text = badge,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite
                                    )
                                }

                                Text(
                                    text = name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isGrandPrix) MonoWhite else MonoSilver
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = MonoMuted, modifier = Modifier.size(12.dp))
                                Text(
                                    text = time,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = MonoSilver
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
private fun CircuitGeometryCard(
    race: Race,
    isWidescreen: Boolean
) {
    val circuitMapUrl = getCircuitMapUrl(race.circuit.circuitId.ifBlank { race.circuit.circuitName })
    val flagColor = FlagColorPalette.getCountryFlagColor(race.circuit.country)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, tint = flagColor, modifier = Modifier.size(16.dp))
                    Text(
                        text = "/ CIRCUIT RADAR (F1DB VECTOR) /",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = ObsidianVoid,
                    border = BorderStroke(1.dp, HairlineBorder)
                ) {
                    Text(
                        text = (if (race.circuit.circuitId.isNotBlank()) race.circuit.circuitId else race.circuit.circuitName).uppercase(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = flagColor
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ObsidianVoid,
                border = BorderStroke(1.dp, HairlineBorder),
                modifier = if (isWidescreen) Modifier.fillMaxWidth().height(260.dp) else Modifier.fillMaxWidth().height(220.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircuitVectorFallback(
                        circuitSlug = race.circuit.circuitId.ifBlank { race.circuit.circuitName },
                        circuitUrl = circuitMapUrl,
                        accentColor = flagColor,
                        isLoading = false,
                        showGrid = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CircuitTechnicalBentoCard(race: Race) {
    val specs = remember(race.circuit.circuitId) {
        getCircuitSpecs(race.circuit.circuitId.ifBlank { race.circuit.circuitName })
    }
    val flagColor = FlagColorPalette.getCountryFlagColor(race.circuit.country)

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = DarkCard,
        border = BorderStroke(1.dp, GlassBorderActive),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsScore,
                        contentDescription = null,
                        tint = F1Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "/ TECHNICAL SPECIFICATIONS /",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = flagColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, flagColor)
                ) {
                    Text(
                        text = specs.direction.uppercase(),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = flagColor
                    )
                }
            }

            // 3-Tile Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecBentoTile("TURNS", "${specs.turns}", modifier = Modifier.weight(1f))
                SpecBentoTile("DRS ZONES", "${specs.drsZones}", modifier = Modifier.weight(1f))
                SpecBentoTile("LENGTH", specs.lengthKm, modifier = Modifier.weight(1f))
            }

            // 3-Tile Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecBentoTile("LAPS", "${specs.laps}", modifier = Modifier.weight(1f))
                SpecBentoTile("DISTANCE", specs.raceDistanceKm, modifier = Modifier.weight(1f))
                SpecBentoTile("FIRST GP", "${specs.firstGrandPrix}", modifier = Modifier.weight(1f))
            }

            // Lap Record Highlight
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = ObsidianVoid,
                border = BorderStroke(1.dp, HairlineBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0x22C040FB),
                            border = BorderStroke(1.dp, Color(0x66C040FB))
                        ) {
                            Text(
                                text = "LAP RECORD",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFC040FB)
                            )
                        }

                        Text(
                            text = specs.lapRecordHolder,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MonoSilver
                        )
                    }

                    Text(
                        text = specs.lapRecordTime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFC040FB)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecBentoTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = ObsidianVoid,
        border = BorderStroke(1.dp, HairlineBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoMuted,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoWhite
            )
        }
    }
}
