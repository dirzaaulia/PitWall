package com.dirzaaulia.formula1.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.Team
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurfaceElevated
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.theme.TextSilver
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.ui.component.MetricTile
import com.dirzaaulia.formula1.util.getTeamCarUrl
import com.dirzaaulia.formula1.util.getTeamLogoUrl

@Composable
fun ConstructorDetailModal(
    team: Team,
    onDismiss: () -> Unit
) {
    val teamGradient = Brush.verticalGradient(
        colors = listOf(
            team.color.copy(alpha = 0.35f),
            GlassSurfaceElevated
        )
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, color = F1Red)
            }
        },
        containerColor = GlassSurfaceElevated,
        shape = RoundedCornerShape(20.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(teamGradient)
                    .border(1.dp, team.color.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FORMULA 1 CONSTRUCTOR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                        FormulaTrackrImage(
                            url = getTeamLogoUrl(team.teamName),
                            contentDescription = team.teamName,
                            removeWhiteBackground = false,
                            modifier = Modifier
                                .height(28.dp)
                                .width(56.dp)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = team.teamName.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = TextPure,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(team.color)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${team.powerUnit} • ${team.base}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSilver
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    FormulaTrackrImage(
                        url = getTeamCarUrl(team.teamName),
                        contentDescription = "${team.teamName} Car",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricTile(label = "POINTS", value = "${team.points.toInt()}", modifier = Modifier.weight(1f))
                    MetricTile(label = "WINS", value = "${team.wins}", modifier = Modifier.weight(1f))
                    MetricTile(label = "ENGINE", value = team.powerUnit.uppercase(), modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricTile(label = "BASE", value = team.base.uppercase(), modifier = Modifier.weight(1.5f))
                    MetricTile(label = "NATIONALITY", value = team.nationality?.uppercase() ?: "F1", modifier = Modifier.weight(1f))
                }

                if (team.drivers.isNotEmpty()) {
                    Text(
                        text = "DRIVER LINEUP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        team.drivers.forEach { driverStr ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PitchBlack,
                                border = BorderStroke(1.dp, GlassBorderActive),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = driverStr,
                                    modifier = Modifier.padding(10.dp),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPure,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
