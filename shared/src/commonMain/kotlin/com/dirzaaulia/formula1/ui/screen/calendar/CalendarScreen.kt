package com.dirzaaulia.formula1.ui.screen.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import com.dirzaaulia.formula1.model.PodiumResult
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.network.NetworkRepository
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
import com.dirzaaulia.formula1.theme.TelemetryBlue
import com.dirzaaulia.formula1.theme.TelemetryGreen
import com.dirzaaulia.formula1.theme.TelemetryYellow
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.ui.component.ShimmerCard
import com.dirzaaulia.formula1.util.FlagColorPalette
import com.dirzaaulia.formula1.util.LocalWindowSizeClass
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.getCurrentDateIso
import com.dirzaaulia.formula1.util.SessionCountdownScheduler
import com.dirzaaulia.formula1.util.calculateCountdownRemaining
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay

private val GoldPodium = Color(0xFFFFD700)
private val SilverPodium = Color(0xFFC8CCD4)
private val BronzePodium = Color(0xFFCD7F32)

@Composable
fun CalendarScreen(
    selectedSeason: Int,
    onSeasonChanged: (Int) -> Unit,
    onSelectRace: (Race) -> Unit
) {
    var isLoading by remember(selectedSeason) { mutableStateOf(true) }
    var races by remember(selectedSeason) { mutableStateOf<List<Race>>(emptyList()) }
    var seasonResults by remember(selectedSeason) { mutableStateOf<Map<Int, List<PodiumResult>>>(emptyMap()) }
    var showYearPicker by remember { mutableStateOf(false) }
    var selectedDecade by remember { mutableStateOf<Int?>(null) }
    val availableYears = remember { (1950..2026).toList().reversed() }

    val filteredYears = remember(selectedDecade) {
        if (selectedDecade == null) availableYears
        else availableYears.filter { it in selectedDecade!!..(selectedDecade!! + 9) }
    }

    LaunchedEffect(selectedSeason) {
        isLoading = true
        races = NetworkRepository.getRaces(selectedSeason)
        seasonResults = NetworkRepository.getSeasonResults(selectedSeason)
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sleek Pit-Wall Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "RACING CALENDAR",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "PitWall • $selectedSeason FIA Formula One World Championship",
                        fontSize = 12.sp,
                        color = MonoMuted
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GlassSurface,
                    border = BorderStroke(1.dp, GlassBorderActive),
                    modifier = Modifier.clickable { showYearPicker = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = selectedSeason.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Season",
                            tint = MonoWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = F1RedSubtle,
                    border = BorderStroke(1.dp, F1Red.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${races.size} ROUNDS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite
                    )
                }

                val today = remember { getCurrentDateIso() }
                val isPastSeason = remember(selectedSeason) { selectedSeason < GMTDate().year }
                val isFutureSeason = remember(selectedSeason) { selectedSeason > GMTDate().year }

                val completedCount = remember(races, isPastSeason, isFutureSeason, today) {
                    if (isPastSeason) races.size
                    else if (isFutureSeason) 0
                    else races.count { it.date.isNotBlank() && it.date < today }
                }

                if (completedCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0x1800D2BE),
                        border = BorderStroke(1.dp, Color(0x4400D2BE))
                    ) {
                        Text(
                            text = "$completedCount FINISHED",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00D2BE)
                        )
                    }
                }

                val upcomingCount = races.size - completedCount
                if (upcomingCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0x18FF8000),
                        border = BorderStroke(1.dp, Color(0x44FF8000))
                    ) {
                        Text(
                            text = "$upcomingCount UPCOMING",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFFFF8000)
                        )
                    }
                }
            }
        }

        if (showYearPicker) {
            @OptIn(ExperimentalMaterial3Api::class)
            ModalBottomSheet(
                onDismissRequest = { showYearPicker = false },
                containerColor = ObsidianSurface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "SELECT SEASON",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoMuted
                    )

                    // Decade filter chips row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            val isAll = selectedDecade == null
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isAll) F1Red else GlassSurface,
                                border = BorderStroke(1.dp, if (isAll) F1Red else GlassBorderActive),
                                modifier = Modifier.clickable { selectedDecade = null }
                            ) {
                                Text(
                                    text = "ALL YEARS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        val decadeList = listOf(2020, 2010, 2000, 1990, 1980, 1970, 1960, 1950)
                        items(decadeList) { d ->
                            val isSelected = selectedDecade == d
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) F1Red else GlassSurface,
                                border = BorderStroke(1.dp, if (isSelected) F1Red else GlassBorderActive),
                                modifier = Modifier.clickable { selectedDecade = d }
                            ) {
                                Text(
                                    text = "${d}s",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // 4-column Grid for years
                    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        items(filteredYears.size) { index ->
                            val year = filteredYears[index]
                            val isSelected = year == selectedSeason
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) F1Red.copy(alpha = 0.2f) else GlassSurface,
                                border = BorderStroke(1.dp, if (isSelected) F1Red else GlassBorderActive),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSeasonChanged(year)
                                        showYearPicker = false
                                    }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$year",
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        color = if (isSelected) F1Red else MonoWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        val windowSizeClass = LocalWindowSizeClass.current
        val isWidescreen = windowSizeClass.isWidescreen
        val gridColumns = if (isWidescreen) GridCells.Adaptive(minSize = 390.dp) else GridCells.Fixed(1)

        if (isLoading) {
            LazyVerticalGrid(
                columns = gridColumns,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(if (isWidescreen) 8 else 6) {
                    ShimmerCard(height = if (isWidescreen) 220.dp else 160.dp)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = gridColumns,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                val today = getCurrentDateIso()
                val isPastSeason = selectedSeason < GMTDate().year
                val isFutureSeason = selectedSeason > GMTDate().year

                val nextUpcomingRace = if (isPastSeason) null
                else if (isFutureSeason) races.firstOrNull()
                else races.firstOrNull { it.date.isNotBlank() && it.date >= today } ?: races.lastOrNull()

                // Featured Event Banner at top (real upcoming race or earliest round for past seasons)
                if (races.isNotEmpty()) {
                    val featuredRace = nextUpcomingRace ?: races.first()
                    val featuredPodium = seasonResults[featuredRace.round]
                    val isFeaturedFinished = isPastSeason || (featuredRace.date.isNotBlank() && featuredRace.date < today)
                    val isFeaturedNextUpcoming = !isPastSeason && (nextUpcomingRace?.round == featuredRace.round)

                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                        FeaturedGrandPrixBanner(
                            race = featuredRace,
                            podium = featuredPodium,
                            isFinished = isFeaturedFinished,
                            isNextUpcoming = isFeaturedNextUpcoming,
                            isWidescreen = isWidescreen,
                            onClick = { onSelectRace(featuredRace) }
                        )
                    }
                }

                itemsIndexed(races) { _, race ->
                    val isRaceFinished = isPastSeason || (race.date.isNotBlank() && race.date < today)
                    val isNextUpcoming = !isPastSeason && (nextUpcomingRace?.round == race.round)
                    val podium = seasonResults[race.round]
                    RaceBentoGridCard(
                        race = race,
                        podium = podium,
                        isFinished = isRaceFinished,
                        isNextUpcoming = isNextUpcoming,
                        isWidescreen = isWidescreen,
                        onClick = { onSelectRace(race) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarCountdownPill(race: Race) {
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
        shape = RoundedCornerShape(8.dp),
        color = Color(0xDD090C12),
        border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(F1Red)
            )
            Text(
                text = upcomingSession.sessionBadge,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = F1Red,
                letterSpacing = 1.sp
            )
            Text(
                text = "${format2Digits(countdown.days)}D : ${format2Digits(countdown.hours)}H : ${format2Digits(countdown.minutes)}M : ${format2Digits(countdown.seconds)}S",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoWhite,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
private fun FeaturedGrandPrixBanner(
    race: Race,
    podium: List<PodiumResult>?,
    isFinished: Boolean,
    isNextUpcoming: Boolean,
    isWidescreen: Boolean,
    onClick: () -> Unit
) {
    val flagColor = FlagColorPalette.getCountryFlagColor(race.circuit.country)
    val accentColor = FlagColorPalette.getCountryAccentColor(race.circuit.country)

    val backgroundBrush = if (isFinished) {
        Brush.horizontalGradient(
            listOf(
                flagColor.copy(alpha = 0.16f),
                Color(0xFF0F1118),
                ObsidianSurfaceElevated
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                flagColor.copy(alpha = 0.32f),
                accentColor.copy(alpha = 0.12f),
                ObsidianSurfaceElevated
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(if (isWidescreen) 20.dp else 14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(if (isWidescreen) 20.dp else 14.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurfaceElevated),
        border = BorderStroke(1.dp, if (isNextUpcoming) F1Red else if (isFinished) flagColor.copy(alpha = 0.35f) else flagColor.copy(alpha = 0.65f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundBrush)
                .padding(if (isWidescreen) 24.dp else 14.dp)
        ) {
            // Subtle Silhouette Flag Watermark
            FormulaTrackrImage(
                url = getCountryFlagUrl(race.circuit.country),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(if (isWidescreen) 260.dp else 130.dp)
                    .graphicsLayer { alpha = if (isFinished) 0.05f else 0.10f }
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(if (isWidescreen) 14.dp else 10.dp)
            ) {
                // Top Badges Row: Clean, Wrap-safe, Well-proportioned
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isNextUpcoming) F1Red else if (isFinished) Color(0x22FFFFFF) else flagColor.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, if (isNextUpcoming) F1Red else if (isFinished) Color(0x44FFFFFF) else flagColor)
                        ) {
                            Text(
                                text = if (isWidescreen) {
                                    if (isNextUpcoming) "/ ROUND ${format2Digits(race.round)} • NEXT UPCOMING ROUND /"
                                    else if (isFinished) "/ ROUND ${format2Digits(race.round)} • OFFICIAL RESULT /"
                                    else "/ ROUND ${format2Digits(race.round)} • CALENDAR /"
                                } else {
                                    if (isNextUpcoming) "ROUND ${format2Digits(race.round)} • NEXT EVENT"
                                    else if (isFinished) "ROUND ${format2Digits(race.round)} • FINISHED"
                                    else "ROUND ${format2Digits(race.round)}"
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = if (isWidescreen) 10.sp else 9.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite,
                                letterSpacing = 0.5.sp
                            )
                        }

                        if (race.isSprint) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = F1Red,
                                border = BorderStroke(1.dp, F1Red)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Sprint",
                                        tint = MonoWhite,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = "SPRINT",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(race.circuit.country),
                            contentDescription = race.circuit.country,
                            modifier = Modifier
                                .height(if (isWidescreen) 18.dp else 14.dp)
                                .width(if (isWidescreen) 28.dp else 22.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )

                        Icon(
                            imageVector = if (race.condition == "RAIN") Icons.Default.WaterDrop else Icons.Default.WbSunny,
                            contentDescription = race.condition,
                            tint = if (race.condition == "RAIN") TelemetryBlue else TelemetryYellow,
                            modifier = Modifier.size(if (isWidescreen) 16.dp else 14.dp)
                        )
                    }
                }

                // Grand Prix Title
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = race.raceName.uppercase(),
                        fontSize = if (isWidescreen) 24.sp else 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 0.5.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = flagColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${race.circuit.circuitName} • ${race.circuit.location}, ${race.circuit.country}",
                            fontSize = if (isWidescreen) 12.sp else 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MonoSilver,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Podium Snippet (for finished) OR Weekend Preview (for upcoming)
                if (isFinished && !podium.isNullOrEmpty()) {
                    BannerPodiumSnippet(podium = podium, isWidescreen = isWidescreen)
                } else if (isWidescreen) {
                    // Widescreen Single Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isNextUpcoming) {
                            CalendarCountdownPill(race = race)
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ObsidianVoid,
                            border = BorderStroke(1.dp, HairlineBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = MonoMuted, modifier = Modifier.size(12.dp))
                                Text(
                                    text = "DATE: ${race.date.ifBlank { "TBD" }}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = MonoWhite
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
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MonoMuted
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = flagColor.copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, flagColor.copy(alpha = 0.7f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "VIEW EVENT HUB",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = MonoWhite
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = MonoWhite,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                } else {
                    // Mobile Compact Stack: Countdown on top, Chips + Action below
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isNextUpcoming) {
                            CalendarCountdownPill(race = race)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ObsidianVoid,
                                    border = BorderStroke(1.dp, HairlineBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Schedule, contentDescription = null, tint = MonoMuted, modifier = Modifier.size(11.dp))
                                        Text(
                                            text = race.date.ifBlank { "TBD" },
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = MonoWhite
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ObsidianVoid,
                                    border = BorderStroke(1.dp, HairlineBorder)
                                ) {
                                    Text(
                                        text = "${race.totalLaps} LAPS",
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = MonoMuted
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = flagColor.copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, flagColor.copy(alpha = 0.7f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "VIEW HUB",
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Black,
                                        color = MonoWhite
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = MonoWhite,
                                        modifier = Modifier.size(11.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BannerPodiumSnippet(
    podium: List<PodiumResult>,
    isWidescreen: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x28000000),
        border = BorderStroke(1.dp, Color(0x22FFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = GoldPodium,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "OFFICIAL PODIUM CLASSIFICATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = MonoMuted,
                    letterSpacing = 0.8.sp
                )
            }

            if (isWidescreen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    podium.take(3).forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            PodiumMicroPill(item = item)
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    podium.take(3).forEach { item ->
                        PodiumMicroPill(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun RaceBentoGridCard(
    race: Race,
    podium: List<PodiumResult>?,
    isFinished: Boolean,
    isNextUpcoming: Boolean,
    isWidescreen: Boolean,
    onClick: () -> Unit
) {
    val flagColor = FlagColorPalette.getCountryFlagColor(race.circuit.country)

    // Vibrant flag gradient for upcoming races; Darker obsidian "hover style" for finished races
    val cardBackgroundBrush = if (isNextUpcoming) {
        Brush.linearGradient(
            listOf(
                F1Red.copy(alpha = 0.28f),
                flagColor.copy(alpha = 0.16f),
                Color(0xFF0F1118)
            )
        )
    } else if (isFinished) {
        Brush.linearGradient(
            listOf(
                flagColor.copy(alpha = 0.12f),
                Color(0xFF0C0E14),
                Color(0xFF07080D)
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                flagColor.copy(alpha = 0.24f),
                flagColor.copy(alpha = 0.06f),
                Color(0xFF0F1118)
            )
        )
    }

    val cardBorder = if (isNextUpcoming) {
        BorderStroke(1.5.dp, F1Red)
    } else if (isFinished) {
        BorderStroke(1.dp, flagColor.copy(alpha = 0.22f))
    } else {
        BorderStroke(1.dp, flagColor.copy(alpha = 0.5f))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
        border = cardBorder
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackgroundBrush)
        ) {
            // Country Flag Silhouette Watermark (No SVG wireframe!)
            FormulaTrackrImage(
                url = getCountryFlagUrl(race.circuit.country),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(if (isWidescreen) 150.dp else 120.dp)
                    .graphicsLayer { alpha = if (isFinished) 0.07f else 0.14f }
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isWidescreen) 18.dp else 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header: Round Pill, Status Badge (FINISHED / UPCOMING / NEXT ROUND), Flag & Weather
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                            color = if (isNextUpcoming) F1Red else if (isFinished) Color(0x22FFFFFF) else flagColor.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, if (isNextUpcoming) F1Red else if (isFinished) Color(0x44FFFFFF) else flagColor)
                        ) {
                            Text(
                                text = "/ R${format2Digits(race.round)} /",
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite
                            )
                        }

                        if (isNextUpcoming) {
                            Surface(
                                shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                                color = F1RedSubtle,
                                border = BorderStroke(1.dp, F1Red)
                            ) {
                                Text(
                                    text = "/ NEXT ROUND /",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                            }
                        } else if (isFinished) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0x2000D2BE),
                                border = BorderStroke(1.dp, Color(0x5500D2BE))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF00D2BE))
                                    )
                                    Text(
                                        text = "FINISHED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFF00D2BE)
                                    )
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = F1RedSubtle,
                                border = BorderStroke(1.dp, F1Red.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "UPCOMING",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                            }
                        }

                        if (race.isSprint) {
                            Surface(
                                shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                                color = F1Red,
                                border = BorderStroke(1.dp, F1Red)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Sprint",
                                        tint = MonoWhite,
                                        modifier = Modifier.size(9.dp)
                                    )
                                    Text(
                                        text = "SPRINT",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FormulaTrackrImage(
                            url = getCountryFlagUrl(race.circuit.country),
                            contentDescription = race.circuit.country,
                            modifier = Modifier
                                .height(15.dp)
                                .width(24.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )

                        Icon(
                            imageVector = if (race.condition == "RAIN") Icons.Default.WaterDrop else Icons.Default.WbSunny,
                            contentDescription = race.condition,
                            tint = if (race.condition == "RAIN") TelemetryBlue else TelemetryYellow,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Grand Prix Name & Circuit
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = race.raceName,
                        fontSize = if (isWidescreen) 17.sp else 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = flagColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${race.circuit.circuitName} • ${race.circuit.country}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MonoSilver,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Middle Section: Countdown for Next Upcoming; Podium for Finished; Telemetry Specs for Upcoming
                if (isFinished && !podium.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x22000000))
                            .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        podium.take(3).forEach { item ->
                            PodiumMicroPill(item = item)
                        }
                    }
                } else if (isNextUpcoming) {
                    CalendarCountdownPill(race = race)
                } else {
                    // Upcoming Race: Track & Schedule telemetry pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x22000000))
                            .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL DISTANCE",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MonoMuted
                            )
                            Text(
                                text = "${race.totalLaps} LAPS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "TRACK LENGTH",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MonoMuted
                            )
                            Text(
                                text = race.trackLength,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = flagColor
                            )
                        }
                    }
                }

                // Bottom Row: Date & Action Callout with real Vector Arrow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = race.date.ifBlank { "TBD" },
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MonoWhite
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isFinished) "VIEW RESULTS" else "CIRCUIT INFO",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = if (isFinished) MonoMuted else flagColor
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = if (isFinished) MonoMuted else flagColor,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumMicroPill(item: PodiumResult) {
    val (medalColor, medalLabel) = when (item.position) {
        1 -> Pair(GoldPodium, "P1")
        2 -> Pair(SilverPodium, "P2")
        else -> Pair(BronzePodium, "P3")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x18FFFFFF))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = medalColor.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, medalColor.copy(alpha = 0.8f))
            ) {
                Text(
                    text = medalLabel,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = medalColor
                )
            }

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(item.teamColor)
            )

            Text(
                text = item.code,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoWhite
            )

            Text(
                text = item.name,
                fontSize = 11.sp,
                color = MonoSilver,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = item.timeOrStatus,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = if (item.position == 1) medalColor else MonoMuted
        )
    }
}
