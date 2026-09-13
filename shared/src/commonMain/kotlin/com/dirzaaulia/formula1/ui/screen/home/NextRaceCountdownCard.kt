package com.dirzaaulia.formula1.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.theme.AccentRed
import com.dirzaaulia.formula1.theme.DarkCard
import com.dirzaaulia.formula1.theme.DarkCardElevated
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurface
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.theme.TextSilver
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.util.calculateCountdownRemaining
import com.dirzaaulia.formula1.util.format2Digits
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.parseUtcDateTimeToEpochMillis
import androidx.compose.foundation.shape.CircleShape
import com.dirzaaulia.formula1.util.SessionCountdownScheduler
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay

@Composable
fun NextRaceCountdownCard(
    race: Race,
    onCalendarClick: () -> Unit,
    isCompact: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onCalendarClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Country Flag Silhouette Watermark
            FormulaTrackrImage(
                url = getCountryFlagUrl(race.circuit.country),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(if (isCompact) 130.dp else 240.dp)
                    .graphicsLayer { alpha = 0.14f }
                    .clip(RoundedCornerShape(16.dp))
            )

            // Red glow gradient on left edge
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                F1Red.copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = 400f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (isCompact) 16.dp else 24.dp),
                verticalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 16.dp)
            ) {
                // Top Header Deck: Slanted Badges & Race Date
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
                            color = F1RedSubtle,
                            border = BorderStroke(1.dp, F1Red.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = "/ ROUND ${format2Digits(race.round)} • 2026 CALENDAR /",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = if (isCompact) 9.sp else 10.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = TextPure,
                                letterSpacing = 1.sp
                            )
                        }

                        if (race.isSprint) {
                            Surface(
                                shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                                color = F1Red,
                                border = BorderStroke(1.dp, F1Red)
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
                                        modifier = Modifier.size(if (isCompact) 10.dp else 12.dp)
                                    )
                                    Text(
                                        text = "SPRINT",
                                        fontSize = if (isCompact) 8.sp else 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextPure
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(ObsidianSurface, RoundedCornerShape(6.dp))
                            .border(1.dp, GlassBorderActive, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = F1Red,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = race.date.ifBlank { "2026 Season" },
                            fontSize = if (isCompact) 10.sp else 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MonoWhite
                        )
                    }
                }

                // Grand Prix Title & Location
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = race.raceName.uppercase().ifBlank { "SPANISH GRAND PRIX" },
                        fontSize = if (isCompact) 20.sp else 28.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 0.5.sp,
                        lineHeight = if (isCompact) 24.sp else 32.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = F1Red,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${race.circuit.circuitName} • ${race.circuit.country}".uppercase(),
                            fontSize = if (isCompact) 11.sp else 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextSilver,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Circuit Technical Spec Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircuitStatPill(label = "CIRCUIT", value = race.circuit.circuitId.ifBlank { "F1 Track" }.uppercase())
                    CircuitStatPill(label = "WEEKEND", value = if (race.isSprint) "SPRINT + RACE" else "PRACTICE • QUALI • RACE")
                }

                // Live Countdown Clock & Circuit CTA Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    CountdownTimerDeck(race = race, isCompact = isCompact)

                    if (!isCompact) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .background(DarkCardElevated, RoundedCornerShape(8.dp))
                                .border(1.dp, F1Red.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.SportsScore,
                                contentDescription = null,
                                tint = F1Red,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "CIRCUIT INTEL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = MonoWhite,
                                letterSpacing = 1.sp
                            )
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = F1Red,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CircuitStatPill(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = ObsidianSurface,
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = label,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = TextMuted
            )
            Text(
                text = value,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MonoSilver
            )
        }
    }
}

@Composable
private fun CountdownTimerDeck(race: Race, isCompact: Boolean = false) {
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

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        // Session identifier banner
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(F1Red)
            )
            Text(
                text = "/ NEXT: ${upcomingSession.sessionName.uppercase()} /",
                fontSize = if (isCompact) 8.sp else 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = F1Red,
                letterSpacing = 1.sp
            )
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = PitchBlack.copy(alpha = 0.85f),
            border = BorderStroke(1.dp, GlassBorderActive)
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = if (isCompact) 10.dp else 14.dp,
                    vertical = if (isCompact) 6.dp else 10.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountdownDigitBlock(value = countdown.days, label = "DAYS", isCompact = isCompact)
                Text(":", fontSize = if (isCompact) 14.sp else 18.sp, fontWeight = FontWeight.Black, color = F1Red)
                CountdownDigitBlock(value = countdown.hours, label = "HRS", isCompact = isCompact)
                Text(":", fontSize = if (isCompact) 14.sp else 18.sp, fontWeight = FontWeight.Black, color = F1Red)
                CountdownDigitBlock(value = countdown.minutes, label = "MINS", isCompact = isCompact)
                Text(":", fontSize = if (isCompact) 14.sp else 18.sp, fontWeight = FontWeight.Black, color = F1Red)
                CountdownDigitBlock(value = countdown.seconds, label = "SECS", isCompact = isCompact)
            }
        }
    }
}

@Composable
private fun CountdownDigitBlock(value: Int, label: String, isCompact: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = format2Digits(value),
            fontSize = if (isCompact) 16.sp else 22.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = MonoWhite
        )
        Text(
            text = label,
            fontSize = if (isCompact) 7.sp else 8.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = TextMuted,
            letterSpacing = 0.5.sp
        )
    }
}

