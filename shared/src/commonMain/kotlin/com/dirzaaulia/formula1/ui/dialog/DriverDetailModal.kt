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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.dirzaaulia.formula1.model.Driver
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.GlassBorder
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurfaceElevated
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.theme.TextSilver
import com.dirzaaulia.formula1.ui.component.FormulaTrackrImage
import com.dirzaaulia.formula1.ui.component.MetricTile
import com.dirzaaulia.formula1.util.getCountryFlagUrl
import com.dirzaaulia.formula1.util.getDriverHeadshotUrl
import com.dirzaaulia.formula1.util.getTeamLogoUrl

@Composable
fun DriverDetailModal(
    driver: Driver,
    onDismiss: () -> Unit
) {
    val teamGradient = Brush.verticalGradient(
        colors = listOf(
            driver.teamColor.copy(alpha = 0.35f),
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
                    .border(1.dp, driver.teamColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FormulaTrackrImage(
                        url = getDriverHeadshotUrl(driver.code),
                        contentDescription = driver.fullName,
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(PitchBlack)
                            .border(2.dp, driver.teamColor, CircleShape)
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (driver.number > 0) {
                                Text(
                                    text = "#${driver.number}",
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFFD700)
                                )
                            }
                            if (driver.code.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = F1RedSubtle,
                                    border = BorderStroke(1.dp, F1Red.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = driver.code,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextPure
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = driver.fullName.uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = TextPure,
                            letterSpacing = 0.5.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FormulaTrackrImage(
                                url = getCountryFlagUrl(driver.nationality),
                                contentDescription = driver.nationality,
                                modifier = Modifier
                                    .height(12.dp)
                                    .width(18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Text(
                                text = "${driver.team} • ${driver.nationality.ifBlank { "F1 Driver" }}",
                                fontSize = 11.sp,
                                color = TextSilver
                            )
                        }
                    }
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                HorizontalDivider(color = GlassBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricTile(label = "POINTS", value = "${driver.points.toInt()}", modifier = Modifier.weight(1f))
                    MetricTile(label = "WINS", value = "${driver.wins}", modifier = Modifier.weight(1f))
                    MetricTile(label = "NATIONALITY", value = driver.nationality.ifBlank { "F1" }.uppercase(), modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricTile(label = "BIRTHDAY", value = driver.birthday.ifBlank { "N/A" }, modifier = Modifier.weight(1f))
                    MetricTile(label = "CODE", value = driver.code.ifBlank { "F1" }, modifier = Modifier.weight(1f))
                    MetricTile(label = "NUMBER", value = if (driver.number > 0) "#${driver.number}" else "N/A", modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PitchBlack)
                        .border(1.dp, GlassBorderActive, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FormulaTrackrImage(
                            url = getTeamLogoUrl(driver.team),
                            contentDescription = driver.team,
                            removeWhiteBackground = false,
                            modifier = Modifier.height(24.dp).width(48.dp)
                        )
                        Text(
                            text = "CONSTRUCTOR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                    Text(
                        text = driver.team,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextPure
                    )
                }
            }
        }
    )
}
