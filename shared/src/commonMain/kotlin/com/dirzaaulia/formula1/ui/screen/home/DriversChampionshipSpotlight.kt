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
import com.dirzaaulia.formula1.model.DriverStandings
import com.dirzaaulia.formula1.theme.DarkCard
import com.dirzaaulia.formula1.theme.DarkCardElevated
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurface
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextSilver
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl

private val GoldPodium = Color(0xFFFFD700)
private val SilverPodium = Color(0xFFC0C0C0)
private val BronzePodium = Color(0xFFCD7F32)

@Composable
fun DriversChampionshipSpotlight(
    standings: List<DriverStandings>,
    onViewAll: () -> Unit
) {
    val leader = standings.firstOrNull()
    val p2 = standings.getOrNull(1)
    val p3 = standings.getOrNull(2)
    val leaderPoints = (leader?.points ?: 1.0).coerceAtLeast(1.0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onViewAll() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Deck: Title & View All Action
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
                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                        color = F1Red.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "/ TOP 3 /",
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = F1Red
                        )
                    }

                    Text(
                        text = "DRIVERS STANDINGS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onViewAll() }
                ) {
                    Text(
                        text = "ALL 22 DRIVERS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = F1Red,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = F1Red,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // P1 Leader Showcase Card
            if (leader != null) {
                val driver = leader.driver
                val teamGradient = Brush.horizontalGradient(
                    colors = listOf(
                        driver.teamColor.copy(alpha = 0.35f),
                        DarkCardElevated
                    )
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkCardElevated,
                    border = BorderStroke(1.dp, GoldPodium.copy(alpha = 0.6f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(teamGradient)
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 6.dp, bottomStart = 6.dp),
                                    color = GoldPodium.copy(alpha = 0.18f),
                                    border = BorderStroke(1.dp, GoldPodium)
                                ) {
                                    Text(
                                        text = "👑 P1 WORLD CHAMPIONSHIP LEADER",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = GoldPodium
                                    )
                                }

                                Text(
                                    text = "${leader.points.toInt()} PTS",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = MonoWhite
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                FormulaTrackrImage(
                                    url = getDriverHeadshotUrl(driver.code),
                                    contentDescription = driver.fullName,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(PitchBlack)
                                        .border(2.dp, driver.teamColor, CircleShape)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = driver.surname.uppercase().ifBlank { driver.fullName.uppercase() },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoWhite,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "${driver.fullName} • ${driver.team}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSilver
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ObsidianSurface,
                                    border = BorderStroke(1.dp, GlassBorderActive)
                                ) {
                                    Text(
                                        text = "${leader.wins} WINS",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = MonoSilver
                                    )
                                }
                            }

                            // Points Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(PitchBlack)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .height(4.dp)
                                        .background(driver.teamColor)
                                )
                            }
                        }
                    }
                }
            }

            // P2 & P3 Podium Rows
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (p2 != null) {
                    DriverPodiumRow(
                        standing = p2,
                        position = 2,
                        badgeColor = SilverPodium,
                        leaderPoints = leaderPoints
                    )
                }

                if (p3 != null) {
                    DriverPodiumRow(
                        standing = p3,
                        position = 3,
                        badgeColor = BronzePodium,
                        leaderPoints = leaderPoints
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverPodiumRow(
    standing: DriverStandings,
    position: Int,
    badgeColor: Color,
    leaderPoints: Double
) {
    val driver = standing.driver
    val gap = leaderPoints - standing.points
    val pct = (standing.points / leaderPoints).toFloat().coerceIn(0.1f, 1f)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = ObsidianSurface,
        border = BorderStroke(1.dp, GlassBorderActive)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = "P$position",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = badgeColor
                        )
                    }

                    FormulaTrackrImage(
                        url = getDriverHeadshotUrl(driver.code),
                        contentDescription = driver.fullName,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(PitchBlack)
                            .border(1.dp, driver.teamColor.copy(alpha = 0.8f), CircleShape)
                    )

                    Column {
                        Text(
                            text = driver.surname.uppercase().ifBlank { driver.fullName.uppercase() },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MonoWhite
                        )
                        Text(
                            text = driver.team,
                            fontSize = 10.sp,
                            color = TextSilver
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${standing.points.toInt()} PTS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MonoWhite
                    )
                    Text(
                        text = "-${gap.toInt()} PTS",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }

            // Proportional points bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(PitchBlack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(pct)
                        .height(3.dp)
                        .background(driver.teamColor.copy(alpha = 0.8f))
                )
            }
        }
    }
}

