package com.dirzaaulia.formula1.ui.screen.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
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
import com.dirzaaulia.formula1.model.Driver
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
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.SessionCountdownScheduler
import com.dirzaaulia.formula1.util.calculateCountdownRemaining
import com.dirzaaulia.formula1.util.LocalWindowSizeClass
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl
import com.dirzaaulia.formula1.util.getDriverHeroCutoutUrl
import com.dirzaaulia.formula1.util.getTeamLogoUrl
import kotlinx.coroutines.delay

data class DriverHeroOrigin(
    val country: String,
    val flagEmoji: String,
    val circuitTitle: String,
    val circuitSub: String,
    val circuitSlug: String
)

fun resolveDriverHeroOrigin(driver: Driver, race: Race?): DriverHeroOrigin {
    val nat = driver.nationality.lowercase().trim()
    val surname = driver.surname.lowercase().trim()
    return when {
        nat.contains("brit") || surname.contains("hamilton") || surname.contains("norris") || surname.contains("russell") || surname.contains("bearman") ->
            DriverHeroOrigin("United Kingdom", "🇬🇧", "Silverstone", "England", "silverstone-8")
        nat.contains("dutch") || surname.contains("verstappen") ->
            DriverHeroOrigin("Netherlands", "🇳🇱", "Zandvoort", "Netherlands", "zandvoort-5")
        nat.contains("monegasque") || surname.contains("leclerc") ->
            DriverHeroOrigin("Monaco", "🇲🇨", "Monte Carlo", "Monaco", "monaco-6")
        nat.contains("span") || surname.contains("sainz") || surname.contains("alonso") ->
            DriverHeroOrigin("Spain", "🇪🇸", "Barcelona", "Catalunya", "catalunya-6")
        nat.contains("ital") || surname.contains("antonelli") ->
            DriverHeroOrigin("Italy", "🇮🇹", "Monza", "Brianza", "monza-7")
        nat.contains("austral") || surname.contains("piastri") ->
            DriverHeroOrigin("Australia", "🇦🇺", "Albert Park", "Melbourne", "melbourne-2")
        nat.contains("mexic") || surname.contains("perez") ->
            DriverHeroOrigin("Mexico", "🇲🇽", "Hermanos Rodríguez", "Mexico City", "mexico-city-3")
        nat.contains("french") || surname.contains("gasly") || surname.contains("ocon") || surname.contains("hadjar") ->
            DriverHeroOrigin("France", "🇫🇷", "Paul Ricard", "Le Castellet", "spa-francorchamps-4")
        nat.contains("brazil") || surname.contains("bortoleto") ->
            DriverHeroOrigin("Brazil", "🇧🇷", "Interlagos", "São Paulo", "interlagos-2")
        nat.contains("canad") || surname.contains("stroll") ->
            DriverHeroOrigin("Canada", "🇨🇦", "Gilles Villeneuve", "Montréal", "montreal-6")
        nat.contains("japan") || surname.contains("tsunoda") ->
            DriverHeroOrigin("Japan", "🇯🇵", "Suzuka", "Mie Prefecture", "suzuka-2")
        else ->
            DriverHeroOrigin("United Kingdom", "🇬🇧", "Silverstone", "England", "silverstone-8")
    }
}

@Composable
fun F1HeroShowcase(
    featuredStanding: DriverStandings,
    allStandings: List<DriverStandings>,
    upcomingRace: Race,
    topConstructors: List<ConstructorsStandings> = emptyList(),
    onSelectDriver: (DriverStandings) -> Unit,
    onNavigateToStandings: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToTelemetry: () -> Unit,
    onSelectRace: (Race) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val isWidescreen = windowSizeClass.isWidescreen

    val driver = featuredStanding.driver
    val rawTeamColor = remember(driver.team, featuredStanding.team.teamName) {
        val tName = driver.team.ifBlank { featuredStanding.team.teamName }
        JolpicaNetworkService.getTeamColor(tName)
    }

    val animatedTeamColor by animateColorAsState(
        targetValue = rawTeamColor,
        animationSpec = tween(500),
        label = "heroTeamColor"
    )

    val origin = remember(driver.driverId, driver.surname, driver.nationality) {
        resolveDriverHeroOrigin(driver, upcomingRace)
    }

    val displayPoints = featuredStanding.points.toInt().takeIf { it > 0 } ?: 4987
    val ghostPoint1 = (displayPoints * 0.45).toInt()
    val ghostPoint2 = (displayPoints * 0.75).toInt()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isWidescreen) 680.dp else 580.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(PitchBlack)
            .drawBehind {
                val centerOffset = Offset(size.width * 0.50f, size.height * 0.46f)
                val radialRadius = size.maxDimension * 0.55f
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            animatedTeamColor.copy(alpha = 0.58f),
                            animatedTeamColor.copy(alpha = 0.28f),
                            Color(0xFF0F111A).copy(alpha = 0.65f),
                            PitchBlack
                        ),
                        center = centerOffset,
                        radius = radialRadius
                    )
                )
            }
    ) {

        // 2. Giant Watermark Racing Number in background
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

        Text(
            text = "$driverNumber",
            fontSize = 260.sp,
            fontWeight = FontWeight.Black,
            color = MonoWhite.copy(alpha = 0.08f),
            letterSpacing = (-12).sp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-20).dp, y = (-20).dp)
        )

        // 3. Central Hero Driver Cutout
        val heroCutoutUrl = remember(driver.code, driver.surname) {
            getDriverHeroCutoutUrl(driver.code.ifBlank { driver.surname })
        }

        FormulaTrackrImage(
            url = heroCutoutUrl,
            contentDescription = driver.fullName,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(if (isWidescreen) 620.dp else 520.dp)
                .padding(bottom = 2.dp),
            contentScale = ContentScale.Fit
        )

        // Subtle gradient floor fade so driver cutout dissolves into PitchBlack
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            PitchBlack.copy(alpha = 0.75f),
                            PitchBlack
                        )
                    )
                )
        )

        // 4. TOP LEFT: Country & Circuit Title Stack + Live Countdown Pill
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 32.dp, top = 26.dp)
                .clickable { onSelectRace(upcomingRace) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FormulaTrackrImage(
                    url = getCountryFlagUrl(origin.country),
                    contentDescription = null,
                    modifier = Modifier.size(width = 20.dp, height = 14.dp).clip(RoundedCornerShape(2.dp))
                )
                Text(
                    text = origin.country,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSilver
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = animatedTeamColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, animatedTeamColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "ROUND ${format2Digits(upcomingRace.round)}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = origin.circuitTitle,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MonoWhite,
                lineHeight = 38.sp
            )
            Text(
                text = origin.circuitSub,
                fontSize = 38.sp,
                fontWeight = FontWeight.Light,
                color = TextSilver,
                lineHeight = 38.sp
            )

            Spacer(Modifier.height(10.dp))

            // Live Countdown Pill
            HeroCountdownPill(race = upcomingRace, accentColor = animatedTeamColor)
        }

        // 5. TOP RIGHT: Floating Vector Circuit & Direct Telemetry Access
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 36.dp, top = 20.dp),
            horizontalAlignment = Alignment.End
        ) {
            CircuitVectorFallback(
                circuitSlug = origin.circuitSlug,
                borderless = true,
                accentColor = animatedTeamColor,
                modifier = Modifier.size(width = 280.dp, height = 180.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 2.dp, end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(animatedTeamColor)
                )
                Text(
                    text = "${origin.circuitTitle.uppercase()} CIRCUIT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            // Direct Telemetry Access Button
            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                color = PitchBlack.copy(alpha = 0.75f),
                border = BorderStroke(1.dp, animatedTeamColor.copy(alpha = 0.7f)),
                modifier = Modifier.clickable { onNavigateToTelemetry() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676))
                    )
                    Text(
                        text = "STANDBY • ARCHIVES READY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00E676),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "[ ⚡ LAUNCH LIVE TIMING ]",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // 6. BOTTOM LEFT: Driver Switcher Dock & Giant Editorial Driver Name
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 32.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Interactive Driver Switcher Dock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Team Emblem Crest
                val teamLogoUrl = remember(driver.team, featuredStanding.team.teamName) {
                    getTeamLogoUrl(driver.team.ifBlank { featuredStanding.team.teamName })
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF141620))
                        .border(1.5.dp, animatedTeamColor.copy(alpha = 0.7f), CircleShape)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FormulaTrackrImage(
                        url = teamLogoUrl,
                        contentDescription = "Team Logo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Driver Avatars
                val displayDrivers = remember(allStandings, driver.driverId) {
                    val list = mutableListOf<DriverStandings>()
                    val preferredSurnames = listOf("hamilton", "antonelli", "leclerc", "norris", "verstappen")
                    for (name in preferredSurnames) {
                        allStandings.find { it.driver.surname.contains(name, ignoreCase = true) }?.let {
                            if (!list.contains(it)) list.add(it)
                        }
                    }
                    for (st in allStandings) {
                        if (list.size < 5 && !list.contains(st)) list.add(st)
                    }
                    list.take(5)
                }
                displayDrivers.forEach { st ->
                    val isSelected = st.driver.driverId == driver.driverId
                    val headshot = getDriverHeadshotUrl(st.driver.code.ifBlank { st.driver.surname })
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) animatedTeamColor.copy(alpha = 0.25f) else Color(0xFF161822))
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

                // "All" pill button
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC700))
                        .clickable { onNavigateToStandings() }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PitchBlack
                    )
                }
            }

            // Giant Editorial Driver Typography
            Column {
                val nameFirst = driver.name.ifBlank { "Lewis" }
                val nameLast = driver.surname.ifBlank { "Hamilton" }
                Text(
                    text = nameFirst,
                    fontSize = if (isWidescreen) 62.sp else 46.sp,
                    fontWeight = FontWeight.Bold,
                    color = MonoWhite,
                    lineHeight = if (isWidescreen) 58.sp else 42.sp,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = nameLast,
                    fontSize = if (isWidescreen) 82.sp else 62.sp,
                    fontWeight = FontWeight.Bold,
                    color = MonoWhite,
                    lineHeight = if (isWidescreen) 76.sp else 58.sp,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "P${featuredStanding.position} WORLD CHAMPIONSHIP LEADER • ${driver.team.uppercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = animatedTeamColor,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // 7. BOTTOM RIGHT: Stacked Ghost Points Counter & Top 3 Constructors Snippet
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 40.dp, bottom = 28.dp)
                .clickable { onNavigateToStandings() },
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    text = "Season Points",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSilver
                )
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(13.dp)
                )
            }

            // Stacked ghost numbers leading into the final gold point count
            Text(
                text = "$ghostPoint1",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = MonoWhite.copy(alpha = 0.22f),
                lineHeight = 36.sp,
                letterSpacing = (-2).sp
            )
            Text(
                text = "$ghostPoint2",
                fontSize = 54.sp,
                fontWeight = FontWeight.Black,
                color = MonoWhite.copy(alpha = 0.45f),
                lineHeight = 44.sp,
                letterSpacing = (-2).sp
            )
            Text(
                text = "$displayPoints",
                fontSize = 76.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFFC700),
                lineHeight = 66.sp,
                letterSpacing = (-3).sp
            )

            // Top 3 Constructors Snippet Capsule
            if (topConstructors.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PitchBlack.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, HairlineBorder),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        topConstructors.take(3).forEachIndexed { index, cs ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "P${index + 1}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (index == 0) Color(0xFFFFC700) else if (index == 1) Color(0xFFC0C0C0) else Color(0xFFCD7F32)
                                )
                                Text(
                                    text = cs.team.teamName.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonoWhite
                                )
                                Text(
                                    text = "${cs.points.toInt()}P",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextMuted
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
private fun HeroCountdownPill(race: Race, accentColor: Color) {
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
        shape = RoundedCornerShape(6.dp),
        color = PitchBlack.copy(alpha = 0.75f),
        border = BorderStroke(1.dp, HairlineBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${format2Digits(countdown.days)}D : ${format2Digits(countdown.hours)}H : ${format2Digits(countdown.minutes)}M : ${format2Digits(countdown.seconds)}S",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoWhite,
                letterSpacing = 1.sp
            )
            Text(
                text = upcomingSession.sessionBadge,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = accentColor
            )
        }
    }
}

