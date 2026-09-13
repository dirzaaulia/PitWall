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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.theme.DarkCard
import com.dirzaaulia.formula1.theme.DarkCardElevated
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedGlow
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurface
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TelemetryGreen
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextSilver

@Composable
fun HomeTelemetryHero(
    onLaunch: () -> Unit,
    isCompact: Boolean = false,
    isLiveSessionActive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onLaunch() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            F1RedGlow.copy(alpha = 0.28f),
                            DarkCard
                        )
                    )
                )
                .padding(if (isCompact) 16.dp else 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(if (isCompact) 12.dp else 16.dp)
            ) {
                // Header Deck: Slanted Badge & Pulsing Status Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                        color = F1Red.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "/ RACE INTELLIGENCE HUB /",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = if (isCompact) 9.sp else 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = F1Red,
                            letterSpacing = 1.sp
                        )
                    }

                    // Live Status Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PitchBlack.copy(alpha = 0.8f),
                        border = BorderStroke(
                            1.dp,
                            if (isLiveSessionActive) TelemetryGreen.copy(alpha = 0.8f) else GlassBorderActive
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(if (isLiveSessionActive) TelemetryGreen else TelemetryGreen.copy(alpha = 0.6f))
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (isLiveSessionActive) "LIVE SESSION ACTIVE" else "STANDBY • ARCHIVES READY",
                                fontSize = if (isCompact) 8.sp else 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                color = if (isLiveSessionActive) TelemetryGreen else MonoSilver,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Title & Subtitle
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "PIT WALL TELEMETRY OPERATIONS",
                        fontSize = if (isCompact) 18.sp else 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 0.5.sp,
                        lineHeight = if (isCompact) 22.sp else 28.sp
                    )

                    Text(
                        text = "Real-time 12-car timing matrix, sector delta telemetry, speed trap monitoring, tyre compound wear, and interactive circuit telemetry charts.",
                        fontSize = if (isCompact) 11.sp else 12.sp,
                        color = TextSilver,
                        lineHeight = if (isCompact) 15.sp else 18.sp
                    )
                }

                // Capability Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TelemetrySpecPill(icon = Icons.Default.GridOn, text = "12 CAR MATRIX")
                    TelemetrySpecPill(icon = Icons.Default.Bolt, text = "SECTOR DELTAS")
                    if (!isCompact) {
                        TelemetrySpecPill(icon = Icons.Default.Schedule, text = "2026 ARCHIVES")
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onLaunch,
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = F1Red),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "LAUNCH PIT WALL",
                            fontSize = if (isCompact) 10.sp else 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    if (!isCompact) {
                        Surface(
                            shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 8.dp, bottomStart = 8.dp),
                            color = ObsidianSurface,
                            border = BorderStroke(1.dp, GlassBorderActive),
                            modifier = Modifier
                                .height(40.dp)
                                .clickable { onLaunch() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = MonoSilver,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "REPLAY ARCHIVE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoSilver,
                                    letterSpacing = 0.5.sp
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
private fun TelemetrySpecPill(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = ObsidianSurface,
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MonoSilver,
                    modifier = Modifier.size(11.dp)
                )
            }
            Text(
                text = text,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = MonoSilver,
                letterSpacing = 0.5.sp
            )
        }
    }
}

