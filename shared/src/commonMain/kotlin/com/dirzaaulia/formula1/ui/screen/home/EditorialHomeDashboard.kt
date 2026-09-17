package com.dirzaaulia.formula1.ui.screen.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
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
import com.dirzaaulia.formula1.util.SessionCountdownScheduler
import com.dirzaaulia.formula1.util.calculateCountdownRemaining
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getCircuitSlug
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl
import com.dirzaaulia.formula1.util.getDriverHeroCutoutUrl
import com.dirzaaulia.formula1.util.getTeamCarUrl
import com.dirzaaulia.formula1.util.getTeamLogoUrl
import kotlinx.coroutines.delay

// ══════════════════════════════════════════════════════════════
//  COLOR TOKENS & HELPERS
// ══════════════════════════════════════════════════════════════
private val AccentGold = Color(0xFFFFC700)
private val NeonRed = Color(0xFFE10600)
private val LiveGreen = Color(0xFF00E676)
private val ObsidianBg = Color(0xFF07090E)

private fun resolveRaceCircuitSlug(race: Race): String {
    val raw = "${race.circuit.circuitId} ${race.circuit.circuitName} ${race.circuit.city} ${race.circuit.location} ${race.raceName}".lowercase()
    if (raw.contains("madrid") || raw.contains("madring")) return "madring-1"
    val id = race.circuit.circuitId.ifBlank { race.circuit.circuitName }.ifBlank { race.raceName }
    return getCircuitSlug(id)
}

private fun resolveRaceCountryName(race: Race): String {
    val raw = "${race.circuit.circuitId} ${race.circuit.circuitName} ${race.circuit.city} ${race.circuit.location} ${race.raceName}".lowercase()
    if (raw.contains("madrid") || raw.contains("madring")) return "Spain"
    return race.circuit.country.ifBlank { race.circuit.location }.ifBlank { race.raceName }
}

// ══════════════════════════════════════════════════════════════
//  EDITORIAL HOME DASHBOARD (NO CARDS — FULL CINEMATIC SHOWCASE)
// ══════════════════════════════════════════════════════════════
@Composable
fun EditorialHomeDashboard(
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

    // Selected constructor for constructor showcase (defaults to P1 constructor)
    var selectedConstructor by remember(topConstructors) {
        mutableStateOf(topConstructors.firstOrNull())
    }
    val activeConstructor = selectedConstructor ?: topConstructors.firstOrNull()

    // Dynamic team ambient colors
    val driverTeamColorRaw = remember(heroStanding.driver.team, heroStanding.team.teamName) {
        JolpicaNetworkService.getTeamColor(
            heroStanding.driver.team.ifBlank { heroStanding.team.teamName }
        )
    }
    val animatedDriverColor by animateColorAsState(
        targetValue = driverTeamColorRaw,
        animationSpec = tween(600),
        label = "driverTeamColor"
    )

    val constructorTeamColorRaw = remember(activeConstructor?.team?.teamName) {
        JolpicaNetworkService.getTeamColor(activeConstructor?.team?.teamName ?: "")
    }
    val animatedConstructorColor by animateColorAsState(
        targetValue = constructorTeamColorRaw,
        animationSpec = tween(600),
        label = "constructorTeamColor"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            // Expressive cinematic ambient lighting — NO RIGID CARDS
            .drawBehind {
                // 1. Driver ambient plume (left-center glow)
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            animatedDriverColor.copy(alpha = 0.50f),
                            animatedDriverColor.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.22f, size.height * 0.50f),
                        radius = size.width * 0.45f
                    )
                )
                // 2. Constructor car glow (right-bottom bleed)
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            animatedConstructorColor.copy(alpha = 0.35f),
                            animatedConstructorColor.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.80f, size.height * 0.72f),
                        radius = size.width * 0.42f
                    )
                )
                // 3. Subtle diagonal light wash
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            animatedDriverColor.copy(alpha = 0.05f),
                            animatedConstructorColor.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        start = Offset(0f, size.height * 0.80f),
                        end = Offset(size.width, size.height * 0.20f)
                    )
                )
            }
    ) {
        // Subtle dark gradient floor so cutouts cleanly merge with the base
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, ObsidianBg.copy(alpha = 0.85f), ObsidianBg)
                    )
                )
        )

        if (isWidescreen) {
            // ──────────────────────────────────────────────────
            //  WIDESCREEN: Full editorial spread
            // ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 22.dp)
            ) {
                // ── TOP HEADER ROW: Schedule + Circuit (Left) & Telemetry Hub (Right) ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // TOP-LEFT: Schedule + Circuit SVG right next to it!
                    RaceScheduleWithCircuitInline(
                        race = nextRace,
                        accentColor = animatedDriverColor,
                        onSelectRace = onSelectRace
                    )

                    // TOP-RIGHT: Minimal Telemetry Hub launcher
                    FloatingTelemetryHub(
                        onNavigateToTelemetry = onNavigateToTelemetry
                    )
                }

                Spacer(Modifier.height(12.dp))

                // ── BOTTOM HALF: Driver Showcase (Left) & Constructor Showcase (Right) ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(36.dp)
                ) {
                    // LEFT: Driver Standing Editorial Showcase (cutout + big bold text + big points)
                    Box(modifier = Modifier.weight(1.1f).fillMaxHeight()) {
                        DriverEditorialShowcase(
                            heroStanding = heroStanding,
                            allStandings = allDriverStandings,
                            teamColor = animatedDriverColor,
                            onSelectDriver = onSelectDriver,
                            onNavigateToStandings = onNavigateToStandings
                        )
                    }

                    // RIGHT: Constructor Standing Showcase (big car image + big bold text + big points)
                    Box(modifier = Modifier.weight(0.9f).fillMaxHeight()) {
                        activeConstructor?.let { constructor ->
                            ConstructorEditorialShowcase(
                                activeConstructor = constructor,
                                topConstructors = topConstructors,
                                teamColor = animatedConstructorColor,
                                onSelectConstructor = { selectedConstructor = it },
                                onNavigateToStandings = onNavigateToStandings
                            )
                        }
                    }
                }
            }
        } else {
            // ──────────────────────────────────────────────────
            // ──────────────────────────────────────────────────
            //  COMPACT / MOBILE: Scrollable spread with constructor section on the bottom
            // ──────────────────────────────────────────────────
            val mobileScrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(mobileScrollState)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .padding(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                RaceScheduleWithCircuitInline(
                    race = nextRace,
                    accentColor = animatedDriverColor,
                    onSelectRace = onSelectRace
                )

                FloatingTelemetryHub(
                    onNavigateToTelemetry = onNavigateToTelemetry
                )

                // Drivers Showcase
                DriverEditorialShowcase(
                    heroStanding = heroStanding,
                    allStandings = allDriverStandings,
                    teamColor = animatedDriverColor,
                    onSelectDriver = onSelectDriver,
                    onNavigateToStandings = onNavigateToStandings,
                    isCompact = true
                )

                // Constructors Showcase on the bottom
                activeConstructor?.let { constructor ->
                    ConstructorEditorialShowcase(
                        activeConstructor = constructor,
                        topConstructors = topConstructors,
                        teamColor = animatedConstructorColor,
                        onSelectConstructor = { selectedConstructor = it },
                        onNavigateToStandings = onNavigateToStandings,
                        isCompact = true
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TOP-LEFT: Schedule + Circuit SVG right next to it!
// ══════════════════════════════════════════════════════════════
@Composable
private fun RaceScheduleWithCircuitInline(
    race: Race,
    accentColor: Color,
    onSelectRace: (Race) -> Unit
) {
    val circuitSlug = remember(race.circuit.circuitId, race.circuit.circuitName, race.raceName) {
        resolveRaceCircuitSlug(race)
    }
    val countryName = remember(race.circuit.country, race.circuit.location, race.raceName) {
        resolveRaceCountryName(race)
    }
    val flagUrl = remember(countryName) { getCountryFlagUrl(countryName) }

    val grandPrixName = remember(race.raceName) {
        race.raceName.replace("Grand Prix", "GP").trim()
    }
    val circuitTitle = remember(race.circuit.city, race.circuit.location, race.circuit.circuitName) {
        race.circuit.city.ifBlank { race.circuit.location }.ifBlank { race.circuit.circuitName }.ifBlank { "Next Event" }
    }

    Row(
        modifier = Modifier.clickable { onSelectRace(race) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Left column: Country flag + Round, Big Bold Race Name, Countdown
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            // Country flag + Round pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (flagUrl.isNotBlank()) {
                    FormulaTrackrImage(
                        url = flagUrl,
                        contentDescription = countryName,
                        modifier = Modifier
                            .size(width = 24.dp, height = 16.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = countryName.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSilver,
                    letterSpacing = 1.sp
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = accentColor.copy(alpha = 0.18f),
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "ROUND ${format2Digits(race.round)}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite
                    )
                }
            }

            // Big Bold Event Typography (Reference style: Silverstone / England)
            Text(
                text = circuitTitle,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = MonoWhite,
                lineHeight = 34.sp,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = grandPrixName,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSilver,
                lineHeight = 26.sp
            )

            // Live Countdown
            EditorialCountdownPill(race = race, accentColor = accentColor)
        }

        // Circuit SVG path RIGHT NEXT TO the schedule info!
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            CircuitVectorFallback(
                circuitSlug = circuitSlug,
                borderless = true,
                accentColor = AccentGold,
                modifier = Modifier.size(width = 210.dp, height = 115.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(AccentGold)
                )
                Text(
                    text = "${race.circuit.circuitName.ifBlank { circuitTitle }.uppercase()} CIRCUIT",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TOP-RIGHT: Floating Telemetry Hub Launcher
// ══════════════════════════════════════════════════════════════
@Composable
private fun FloatingTelemetryHub(
    onNavigateToTelemetry: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0F121C).copy(alpha = 0.75f),
        border = BorderStroke(1.dp, HairlineBorder),
        modifier = Modifier.clickable { onNavigateToTelemetry() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(LiveGreen)
            )
            Column {
                Text(
                    text = "PIT WALL LIVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = LiveGreen,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "TELEMETRY ACCESS READY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MonoWhite
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = AccentGold.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "LAUNCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = AccentGold
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  BOTTOM-LEFT: Driver Standing Editorial Showcase
// ══════════════════════════════════════════════════════════════
@Composable
private fun DriverEditorialShowcase(
    heroStanding: DriverStandings,
    allStandings: List<DriverStandings>,
    teamColor: Color,
    onSelectDriver: (DriverStandings) -> Unit,
    onNavigateToStandings: () -> Unit,
    isCompact: Boolean = false
) {
    val driver = heroStanding.driver
    val driverNumber = driver.number.takeIf { it > 0 } ?: when {
        driver.surname.contains("ham", ignoreCase = true) -> 44
        driver.surname.contains("ver", ignoreCase = true) -> 1
        driver.surname.contains("nor", ignoreCase = true) -> 4
        driver.surname.contains("lec", ignoreCase = true) -> 16
        driver.surname.contains("rus", ignoreCase = true) -> 63
        driver.surname.contains("pia", ignoreCase = true) -> 81
        driver.surname.contains("ant", ignoreCase = true) -> 12
        driver.surname.contains("sai", ignoreCase = true) -> 55
        else -> 44
    }

    val heroCutoutUrl = remember(driver.code, driver.surname) {
        getDriverHeroCutoutUrl(driver.code.ifBlank { driver.surname })
    }

    val displayPoints = heroStanding.points.toInt()

    val boxModifier = if (isCompact) {
        Modifier.fillMaxWidth().height(460.dp)
    } else {
        Modifier.fillMaxSize()
    }

    Box(modifier = boxModifier) {
        // 1. Giant Watermark Racing Number in background
        Text(
            text = "$driverNumber",
            fontSize = if (isCompact) 150.sp else 250.sp,
            fontWeight = FontWeight.Black,
            color = MonoWhite.copy(alpha = 0.07f),
            letterSpacing = (-10).sp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-30).dp, y = (-20).dp)
        )

        // 2. Big Driver Cutout Image
        FormulaTrackrImage(
            url = heroCutoutUrl,
            contentDescription = driver.fullName,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(if (isCompact) 320.dp else 490.dp)
                .padding(bottom = 4.dp),
            contentScale = ContentScale.Fit
        )

        // 3. Driver Switcher Dock (Top of the driver zone) - Top 3 Drivers Only
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Team Emblem Crest (Transparent Hi-Res)
            val teamLogoUrl = remember(driver.team, heroStanding.team.teamName) {
                getTeamLogoUrl(driver.team.ifBlank { heroStanding.team.teamName })
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.14f))
                    .border(1.5.dp, teamColor.copy(alpha = 0.7f), CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                FormulaTrackrImage(
                    url = teamLogoUrl,
                    contentDescription = "Team Crest",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Top 3 driver avatar pills
            val displayDrivers = remember(allStandings) {
                allStandings.take(3)
            }

            displayDrivers.forEach { st ->
                val isSelected = st.driver.driverId == driver.driverId
                val headshot = getDriverHeadshotUrl(st.driver.code.ifBlank { st.driver.surname })
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) teamColor.copy(alpha = 0.3f) else Color(0xFF141620))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MonoWhite else HairlineBorder,
                            shape = CircleShape
                        )
                        .clickable { onSelectDriver(st) },
                    contentAlignment = Alignment.Center
                ) {
                    FormulaTrackrImage(
                        url = headshot,
                        contentDescription = st.driver.fullName,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // "All" pill button to Standings
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
                    .background(AccentGold)
                    .clickable { onNavigateToStandings() }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "All",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PitchBlack
                )
            }
        }

        // 4. BIG BOLD DRIVER NAME & SCORE (Bottom-Left Side)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Left: Giant Editorial Name
            Column {
                val nameFirst = driver.name.ifBlank { "Max" }
                val nameLast = driver.surname.ifBlank { "Verstappen" }
                Text(
                    text = nameFirst.uppercase(),
                    fontSize = if (isCompact) 32.sp else 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = MonoWhite,
                    lineHeight = if (isCompact) 30.sp else 46.sp,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = nameLast.uppercase(),
                    fontSize = if (isCompact) 44.sp else 68.sp,
                    fontWeight = FontWeight.Black,
                    color = MonoWhite,
                    lineHeight = if (isCompact) 40.sp else 62.sp,
                    letterSpacing = (-1.5).sp
                )
                Text(
                    text = "P${heroStanding.position} WORLD CHAMPIONSHIP LEADER • ${driver.team.uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = teamColor,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Right: Big Score Text (Current Year Points Only)
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.clickable { onNavigateToStandings() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "2026 SEASON POINTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextSilver,
                        letterSpacing = 0.8.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = "$displayPoints",
                    fontSize = if (isCompact) 56.sp else 74.sp,
                    fontWeight = FontWeight.Black,
                    color = AccentGold,
                    lineHeight = if (isCompact) 48.sp else 64.sp,
                    letterSpacing = (-2.5).sp
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  BOTTOM-RIGHT: Constructor Standing Showcase (Car Image Showcase)
// ══════════════════════════════════════════════════════════════
@Composable
private fun ConstructorEditorialShowcase(
    activeConstructor: ConstructorsStandings,
    topConstructors: List<ConstructorsStandings>,
    teamColor: Color,
    onSelectConstructor: (ConstructorsStandings) -> Unit,
    onNavigateToStandings: () -> Unit,
    isCompact: Boolean = false
) {
    val teamName = activeConstructor.team.teamName.ifBlank { "McLaren" }
    val carImageUrl = remember(teamName) { getTeamCarUrl(teamName) }

    val constructorPoints = activeConstructor.points.toInt()

    val boxModifier = if (isCompact) {
        Modifier.fillMaxWidth().height(420.dp)
    } else {
        Modifier.fillMaxSize()
    }

    Box(modifier = boxModifier) {
        // 1. Giant Watermark Constructor abbreviation or rank
        val watermarkText = remember(teamName) {
            when {
                teamName.contains("mclaren", ignoreCase = true) -> "MCL"
                teamName.contains("red bull", ignoreCase = true) -> "RBR"
                teamName.contains("ferrari", ignoreCase = true) -> "SF"
                teamName.contains("mercedes", ignoreCase = true) -> "MGP"
                teamName.contains("aston", ignoreCase = true) -> "AMR"
                else -> "P${activeConstructor.position}"
            }
        }

        Text(
            text = watermarkText,
            fontSize = if (isCompact) 120.sp else 210.sp,
            fontWeight = FontWeight.Black,
            color = MonoWhite.copy(alpha = 0.05f),
            letterSpacing = (-8).sp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 20.dp, y = (-20).dp)
        )

        // 2. Big Car Image Showcase (6col-retina Ultra Hi-Res)
        FormulaTrackrImage(
            url = carImageUrl,
            contentDescription = "$teamName F1 Car",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(if (isCompact) 170.dp else 250.dp)
                .offset(y = (-10).dp),
            contentScale = ContentScale.Fit
        )

        // 3. Constructor Switcher Dock (Top of the constructor zone)
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topConstructors.take(3).forEach { cs ->
                val isSelected = cs.team.teamId == activeConstructor.team.teamId
                val csColor = JolpicaNetworkService.getTeamColor(cs.team.teamName)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) csColor.copy(alpha = 0.25f) else Color(0xFF141620),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MonoWhite else HairlineBorder
                    ),
                    modifier = Modifier.clickable { onSelectConstructor(cs) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(csColor)
                        )
                        Text(
                            text = "P${cs.position} ${cs.team.teamName.take(10)}",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MonoWhite else TextSilver
                        )
                    }
                }
            }

            // "All" pill
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222736))
                    .border(1.dp, HairlineBorder, CircleShape)
                    .clickable { onNavigateToStandings() }
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "All",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSilver
                )
            }
        }

        // 4. BIG BOLD CONSTRUCTOR NAME & SCORE (Bottom Side)
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Left: Giant Editorial Constructor Name
            Column {
                Text(
                    text = teamName.uppercase(),
                    fontSize = if (isCompact) 34.sp else 54.sp,
                    fontWeight = FontWeight.Black,
                    color = MonoWhite,
                    lineHeight = if (isCompact) 32.sp else 48.sp,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "P${activeConstructor.position} CONSTRUCTORS' CHAMPIONSHIP LEADER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = teamColor,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Right: Big Score Text (Current Year Points Only)
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.clickable { onNavigateToStandings() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "2026 CONSTRUCTOR POINTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextSilver,
                        letterSpacing = 0.8.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = "$constructorPoints",
                    fontSize = if (isCompact) 56.sp else 74.sp,
                    fontWeight = FontWeight.Black,
                    color = AccentGold,
                    lineHeight = if (isCompact) 48.sp else 64.sp,
                    letterSpacing = (-2.5).sp
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  EDITORIAL COUNTDOWN PILL
// ══════════════════════════════════════════════════════════════
@Composable
private fun EditorialCountdownPill(race: Race, accentColor: Color) {
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
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF10131E).copy(alpha = 0.85f),
        border = BorderStroke(1.dp, HairlineBorder),
        modifier = Modifier.padding(top = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
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
                text = "${countdown.days}D ${format2Digits(countdown.hours)}H ${format2Digits(countdown.minutes)}M ${format2Digits(countdown.seconds)}S",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoWhite,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "• ${upcomingSession.sessionBadge}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = AccentGold
            )
        }
    }
}
