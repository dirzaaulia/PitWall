package com.dirzaaulia.formula1.ui.screen.home

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.ConstructorsStandings
import com.dirzaaulia.formula1.model.DriverStandings
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.network.JolpicaNetworkService
import com.dirzaaulia.formula1.theme.HairlineBorder
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextSilver
import com.dirzaaulia.formula1.ui.component.CircuitVectorFallback
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.util.LocalWindowSizeClass
import com.dirzaaulia.formula1.util.calculateCountdownRemaining
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getCircuitSlug
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl
import com.dirzaaulia.formula1.util.getDriverHeroCutoutUrl
import com.dirzaaulia.formula1.util.getTeamCarUrl
import com.dirzaaulia.formula1.util.parseUtcDateTimeToEpochMillis
import com.dirzaaulia.formula1.util.SessionCountdownScheduler
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay

// ══════════════════════════════════════════════════════════════
//  DESIGN SYSTEM CONSTANTS (Synthesized from 3 References)
// ══════════════════════════════════════════════════════════════
private val AccentGold = Color(0xFFFFC700)
private val AccentGoldDim = Color(0xFFB8860B)
private val LiveGreen = Color(0xFF00E676)
private val NeonRed = Color(0xFFE10600)
private val ObsidianPanel = Color(0xFF0A0C14)
private val ObsidianPanelAlt = Color(0xFF0E1019)
private val HairlineWhite = Color(0x1AFFFFFF)  // 10% white hairline
private val TextLabel = Color(0xFF6B7280)

/** Resolves next race's circuit slug from circuit ID or name */
private fun resolveNextRaceCircuitSlug(race: Race): String {
    val id = race.circuit.circuitId.ifBlank { race.circuit.circuitName }.ifBlank { race.raceName }
    return getCircuitSlug(id)
}

/** Resolves race country string for flag lookup */
private fun resolveRaceCountry(race: Race): String {
    return race.circuit.country.ifBlank {
        race.circuit.location.ifBlank { race.raceName }
    }
}

// ══════════════════════════════════════════════════════════════
//  ROOT: PitWall Home Dashboard
// ══════════════════════════════════════════════════════════════
@Composable
fun PitWallHomeDashboard(
    nextRace: Race,
    topDriverStandings: List<DriverStandings>,
    allDriverStandings: List<DriverStandings>,
    topConstructors: List<ConstructorsStandings>,
    heroStanding: DriverStandings,
    onSelectDriver: (DriverStandings) -> Unit,
    onNavigateToTelemetry: () -> Unit,
    onNavigateToStandings: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onSelectRace: (Race) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val isWidescreen = windowSizeClass.isWidescreen

    // Ambient glow driven by HERO DRIVER team color (not by next race)
    val rawHeroColor = remember(heroStanding.driver.team, heroStanding.team.teamName) {
        JolpicaNetworkService.getTeamColor(
            heroStanding.driver.team.ifBlank { heroStanding.team.teamName }
        )
    }
    val heroColor by animateColorAsState(
        targetValue = rawHeroColor,
        animationSpec = tween(700),
        label = "heroTeamAmbient"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            // Expressive multi-point ambient background — not a simple radial circle
            .drawBehind {
                // Primary atmospheric glow — top-left driver area
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            heroColor.copy(alpha = 0.50f),
                            heroColor.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.22f, size.height * 0.38f),
                        radius = size.width * 0.45f
                    )
                )
                // Secondary deep bleed — bottom right
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            heroColor.copy(alpha = 0.28f),
                            heroColor.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.85f, size.height * 0.80f),
                        radius = size.width * 0.38f
                    )
                )
                // Dark obsidian center dampener
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            ObsidianPanel.copy(alpha = 0.55f),
                            ObsidianPanel.copy(alpha = 0.90f)
                        ),
                        center = Offset(size.width * 0.50f, size.height * 0.50f),
                        radius = size.maxDimension * 0.70f
                    )
                )
                // Speed-streak accent: subtle diagonal light wash across the midfield
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            heroColor.copy(alpha = 0.06f),
                            heroColor.copy(alpha = 0.12f),
                            heroColor.copy(alpha = 0.04f),
                            Color.Transparent
                        ),
                        start = Offset(0f, size.height * 0.65f),
                        end = Offset(size.width, size.height * 0.28f)
                    )
                )
            }
    ) {
        if (isWidescreen) {
            // ── WIDESCREEN: full 3-zone layout ──
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 20.dp)) {
                // TOP ROW: Next Race (left) + Telemetry Hub (right)
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // TOP-LEFT: Next Race & Circuit
                    NextRacePanel(
                        race = nextRace,
                        onSelectRace = onSelectRace,
                        modifier = Modifier.weight(1f)
                    )
                    // TOP-RIGHT: Telemetry Access Hub
                    TelemetryHubPanel(
                        onNavigateToTelemetry = onNavigateToTelemetry,
                        modifier = Modifier.weight(0.6f)
                    )
                }

                Spacer(Modifier.height(20.dp))

                // BOTTOM ROW: Driver Standings (left) + Constructor Standings (right)
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // BOTTOM-LEFT: Top 3 Driver Standings
                    DriverStandingsPanel(
                        topDrivers = topDriverStandings,
                        heroStanding = heroStanding,
                        allStandings = allDriverStandings,
                        onSelectDriver = onSelectDriver,
                        onNavigateToStandings = onNavigateToStandings,
                        heroColor = heroColor,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    // BOTTOM-RIGHT: Top 3 Constructor Standings
                    ConstructorStandingsPanel(
                        topConstructors = topConstructors,
                        onNavigateToStandings = onNavigateToStandings,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
        } else {
            // ── COMPACT: stacked layout ──
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                NextRacePanel(race = nextRace, onSelectRace = onSelectRace, modifier = Modifier.fillMaxWidth())
                TelemetryHubPanel(onNavigateToTelemetry = onNavigateToTelemetry, modifier = Modifier.fillMaxWidth())
                DriverStandingsPanel(
                    topDrivers = topDriverStandings,
                    heroStanding = heroStanding,
                    allStandings = allDriverStandings,
                    onSelectDriver = onSelectDriver,
                    onNavigateToStandings = onNavigateToStandings,
                    heroColor = heroColor,
                    modifier = Modifier.fillMaxWidth()
                )
                ConstructorStandingsPanel(
                    topConstructors = topConstructors,
                    onNavigateToStandings = onNavigateToStandings,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ZONE 1: Next Race Panel — Top Left
// ══════════════════════════════════════════════════════════════
@Composable
private fun NextRacePanel(
    race: Race,
    onSelectRace: (Race) -> Unit,
    modifier: Modifier = Modifier
) {
    val circuitSlug = remember(race.circuit.circuitId, race.circuit.circuitName) {
        resolveNextRaceCircuitSlug(race)
    }
    val countryForFlag = remember(race.circuit.country, race.circuit.location) {
        resolveRaceCountry(race)
    }
    val flagUrl = remember(countryForFlag) { getCountryFlagUrl(countryForFlag) }

    Surface(
        modifier = modifier.clickable { onSelectRace(race) },
        shape = RoundedCornerShape(20.dp),
        color = ObsidianPanel.copy(alpha = 0.70f),
        border = BorderStroke(1.dp, HairlineWhite)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left: Race info stack
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // NEXT EVENT label (McLaren Applied style)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(NeonRed)
                    )
                    Text(
                        text = "NEXT EVENT",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = NeonRed,
                        letterSpacing = 2.sp
                    )
                    // Round badge (Shift style: slashed accent)
                    if (race.round > 0) {
                        Text(
                            text = "// RND ${format2Digits(race.round)}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextLabel,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Country flag image + race name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FormulaTrackrImage(
                        url = flagUrl,
                        contentDescription = countryForFlag,
                        modifier = Modifier
                            .width(32.dp)
                            .height(22.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text(
                            text = race.raceName.replace("Grand Prix", "GP").ifBlank { "Formula 1" },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MonoWhite,
                            lineHeight = 24.sp
                        )
                        if (race.circuit.circuitName.isNotBlank()) {
                            Text(
                                text = race.circuit.circuitName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSilver
                            )
                        }
                        if (race.circuit.location.isNotBlank() && race.circuit.country.isNotBlank()) {
                            Text(
                                text = "${race.circuit.location}, ${race.circuit.country}",
                                fontSize = 10.sp,
                                color = TextLabel
                            )
                        }
                    }
                }

                // Race date & track length
                if (race.date.isNotBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Column {
                            Text(
                                text = "RACE DATE",
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextLabel,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = race.date,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite
                            )
                        }
                        if (race.trackLength.isNotBlank() && race.trackLength != "5.278 km") {
                            Column {
                                Text(
                                    text = "TRACK LENGTH",
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextLabel,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = race.trackLength,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Countdown pill
                RaceCountdownPill(race = race)
            }

            // Right: Floating circuit vector
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(180.dp)
            ) {
                CircuitVectorFallback(
                    circuitSlug = circuitSlug,
                    borderless = true,
                    accentColor = NeonRed,
                    modifier = Modifier.size(width = 180.dp, height = 140.dp)
                )
                Text(
                    text = race.circuit.circuitName.ifBlank { circuitSlug.replace("-", " ").uppercase() },
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = TextLabel,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ZONE 2: Telemetry Access Hub — Top Right
// ══════════════════════════════════════════════════════════════
@Composable
private fun TelemetryHubPanel(
    onNavigateToTelemetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "livePulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "liveAlpha"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = ObsidianPanelAlt.copy(alpha = 0.70f),
        border = BorderStroke(1.dp, HairlineWhite)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(LiveGreen.copy(alpha = pulseAlpha))
                    )
                    Text(
                        text = "TELEMETRY ENGINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = LiveGreen.copy(alpha = pulseAlpha),
                        letterSpacing = 2.sp
                    )
                }
                Text(
                    text = "PIT WALL",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MonoWhite,
                    lineHeight = 28.sp
                )
                Text(
                    text = "TIMING & TELEMETRY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSilver,
                    letterSpacing = 0.5.sp
                )
            }

            // Status indicators (McLaren Applied style data pods)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TelemetryStatusRow(label = "LIVE SESSION", value = "STANDBY", isActive = false)
                TelemetryStatusRow(label = "ARCHIVE", value = "READY", isActive = true)
                TelemetryStatusRow(label = "SECTORS", value = "DELTA ENABLED", isActive = true)
            }

            // Launch button — Shift Racing style high-contrast CTA
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTelemetry() },
                shape = RoundedCornerShape(10.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, NeonRed.copy(alpha = 0.80f))
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(NeonRed.copy(alpha = 0.15f), Color.Transparent)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = NeonRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "LAUNCH PIT WALL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite,
                            letterSpacing = 1.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = NeonRed,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TelemetryStatusRow(label: String, value: String, isActive: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            color = TextLabel,
            letterSpacing = 1.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(if (isActive) LiveGreen else TextLabel)
            )
            Text(
                text = value,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (isActive) MonoWhite else TextLabel,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ZONE 3: Driver Standings Panel — Bottom Left
// ══════════════════════════════════════════════════════════════
@Composable
private fun DriverStandingsPanel(
    topDrivers: List<DriverStandings>,
    heroStanding: DriverStandings,
    allStandings: List<DriverStandings>,
    onSelectDriver: (DriverStandings) -> Unit,
    onNavigateToStandings: () -> Unit,
    heroColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = ObsidianPanel.copy(alpha = 0.70f),
        border = BorderStroke(1.dp, HairlineWhite)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header (McLaren Applied precision style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DRIVERS'",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextLabel,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Championship",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MonoWhite
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onNavigateToStandings() }
                ) {
                    Text(
                        text = "TOP 3",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = NeonRed,
                        letterSpacing = 1.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = NeonRed,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Divider
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(HairlineWhite))

            Spacer(Modifier.height(14.dp))

            // Top 3 drivers
            val displayList = if (topDrivers.isEmpty()) {
                listOf(heroStanding)
            } else topDrivers

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                displayList.take(3).forEachIndexed { idx, standing ->
                    val isHero = standing.driver.driverId == heroStanding.driver.driverId
                    DriverStandingRow(
                        position = idx + 1,
                        standing = standing,
                        isSelected = isHero,
                        accentColor = if (isHero) heroColor else TextLabel,
                        onSelectDriver = onSelectDriver
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Leaderboard CTA (bottom)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToStandings() }
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FULL STANDINGS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = TextLabel,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextLabel,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

@Composable
private fun DriverStandingRow(
    position: Int,
    standing: DriverStandings,
    isSelected: Boolean,
    accentColor: Color,
    onSelectDriver: (DriverStandings) -> Unit
) {
    val driver = standing.driver
    val headshotUrl = remember(driver.code, driver.surname) {
        getDriverHeadshotUrl(driver.code.ifBlank { driver.surname })
    }
    val teamColorRaw = remember(driver.team, standing.team.teamName) {
        JolpicaNetworkService.getTeamColor(driver.team.ifBlank { standing.team.teamName })
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) accentColor.copy(alpha = 0.10f)
                else Color.Transparent
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) accentColor.copy(alpha = 0.30f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelectDriver(standing) }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Position watermark (F1-75 editorial ghost style)
        Text(
            text = "P$position",
            fontSize = if (position == 1) 22.sp else 16.sp,
            fontWeight = FontWeight.Black,
            color = if (position == 1) AccentGold else MonoWhite.copy(alpha = 0.35f),
            letterSpacing = (-1).sp,
            modifier = Modifier.width(30.dp)
        )

        // Team color bar (McLaren Applied style)
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(teamColorRaw)
        )

        // Headshot
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(ObsidianPanelAlt)
                .border(1.dp, teamColorRaw.copy(alpha = 0.50f), CircleShape)
        ) {
            FormulaTrackrImage(
                url = headshotUrl,
                contentDescription = driver.fullName,
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Name + Team
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = driver.name.ifBlank { "–" },
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TextSilver,
                lineHeight = 12.sp
            )
            Text(
                text = driver.surname.ifBlank { "–" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MonoWhite,
                lineHeight = 18.sp,
                letterSpacing = (-0.3).sp
            )
            Text(
                text = driver.team.ifBlank { standing.team.teamName }.uppercase(),
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                color = teamColorRaw.copy(alpha = 0.85f),
                letterSpacing = 0.5.sp
            )
        }

        // Points (stacked ghost style inspired by F1-75 reference)
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${standing.points.toInt()}",
                fontSize = if (position == 1) 28.sp else 20.sp,
                fontWeight = FontWeight.Black,
                color = if (position == 1) AccentGold else MonoWhite,
                letterSpacing = (-1).sp
            )
            Text(
                text = "PTS",
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                color = TextLabel,
                letterSpacing = 1.sp
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ZONE 4: Constructor Standings Panel — Bottom Right
// ══════════════════════════════════════════════════════════════
@Composable
private fun ConstructorStandingsPanel(
    topConstructors: List<ConstructorsStandings>,
    onNavigateToStandings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = ObsidianPanelAlt.copy(alpha = 0.70f),
        border = BorderStroke(1.dp, HairlineWhite)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CONSTRUCTORS'",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextLabel,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Championship",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MonoWhite
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onNavigateToStandings() }
                ) {
                    Text(
                        text = "TOP 3",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = NeonRed,
                        letterSpacing = 1.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = NeonRed,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(HairlineWhite))
            Spacer(Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                topConstructors.take(3).forEachIndexed { idx, cs ->
                    ConstructorStandingRow(
                        position = idx + 1,
                        constructorStandings = cs
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToStandings() }
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FULL STANDINGS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = TextLabel,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextLabel,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

@Composable
private fun ConstructorStandingRow(
    position: Int,
    constructorStandings: ConstructorsStandings
) {
    val team = constructorStandings.team
    val teamColor = remember(team.teamName) { JolpicaNetworkService.getTeamColor(team.teamName) }
    val carUrl = remember(team.teamName) { getTeamCarUrl(team.teamName) }
    val positionColor = when (position) {
        1 -> AccentGold
        2 -> Color(0xFFCDD1DB)
        else -> Color(0xFFCD9C55)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(teamColor.copy(alpha = 0.06f))
            .border(1.dp, teamColor.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Position (Shift style bold contrast)
        Text(
            text = "P$position",
            fontSize = if (position == 1) 22.sp else 16.sp,
            fontWeight = FontWeight.Black,
            color = positionColor,
            letterSpacing = (-1).sp,
            modifier = Modifier.width(30.dp)
        )

        // Team color sidebar
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(teamColor)
        )

        // F1 Car image
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF060810))
        ) {
            FormulaTrackrImage(
                url = carUrl,
                contentDescription = "${team.teamName} car",
                modifier = Modifier.fillMaxSize().padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Team name + Chassis
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = team.teamName.uppercase(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MonoWhite,
                letterSpacing = (-0.3).sp,
                lineHeight = 16.sp
            )
            if (!team.nationality.isNullOrBlank()) {
                Text(
                    text = team.nationality.orEmpty(),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextLabel
                )
            }
        }

        // Points
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${constructorStandings.points.toInt()}",
                fontSize = if (position == 1) 26.sp else 18.sp,
                fontWeight = FontWeight.Black,
                color = if (position == 1) AccentGold else MonoWhite,
                letterSpacing = (-1).sp
            )
            Text(
                text = "PTS",
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace,
                color = TextLabel,
                letterSpacing = 1.sp
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  COUNTDOWN PILL — detached from driver, attached to next race
// ══════════════════════════════════════════════════════════════
@Composable
private fun RaceCountdownPill(race: Race) {
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Live dot
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(NeonRed)
        )
        Text(
            text = upcomingSession.sessionBadge,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            color = TextLabel,
            letterSpacing = 2.sp
        )
        Text(
            text = "${format2Digits(countdown.days)}D : ${format2Digits(countdown.hours)}H : ${format2Digits(countdown.minutes)}M : ${format2Digits(countdown.seconds)}S",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = MonoWhite,
            letterSpacing = 1.sp
        )
    }
}
