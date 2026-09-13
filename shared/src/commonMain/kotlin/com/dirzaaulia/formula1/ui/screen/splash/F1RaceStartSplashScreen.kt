package com.dirzaaulia.formula1.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurfaceElevated
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.theme.TelemetryGreen
import com.dirzaaulia.formula1.theme.TextMuted
import com.dirzaaulia.formula1.theme.TextPure
import com.dirzaaulia.formula1.theme.TextSilver
import formulatrackr.shared.generated.resources.ic_monochrome
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun F1RaceStartSplashScreen(
    onSplashFinished: () -> Unit
) {
    var stage by remember { mutableIntStateOf(0) }
    val carTranslationX = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Red lights 1 through 5
        delay(300)
        stage = 1
        delay(400)
        stage = 2
        delay(400)
        stage = 3
        delay(400)
        stage = 4
        delay(400)
        stage = 5
        delay(600)

        // Lights OUT! GREEN!
        stage = 6

        // F1 Car ZOOM AWAY!
        carTranslationX.animateTo(
            targetValue = 1800f,
            animationSpec = tween(durationMillis = 600, easing = FastOutLinearInEasing)
        )

        delay(300)
        stage = 7
        onSplashFinished()
    }

    val splashAlpha by animateFloatAsState(
        targetValue = if (stage == 7) 0f else 1f,
        animationSpec = tween(400)
    )

    AnimatedVisibility(
        visible = stage < 7,
        exit = fadeOut(tween(400))
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(PitchBlack)
                .alpha(splashAlpha),
            contentAlignment = Alignment.Center
        ) {
            val isCompact = maxWidth < 600.dp
            val lightSize = if (isCompact) 42.dp else 54.dp
            val innerFresnelSize = if (isCompact) 26.dp else 34.dp
            val secondaryLedSize = if (isCompact) 12.dp else 16.dp
            val lightSpacing = if (isCompact) 10.dp else 20.dp
            val gantryPadH = if (isCompact) 14.dp else 32.dp
            val outerPadH = if (isCompact) 12.dp else 32.dp

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = outerPadH, vertical = 24.dp)
            ) {
                // Top Protocol Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = F1Red.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, F1Red.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = "PITWALL • OFFICIAL TIMING PROTOCOL",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        fontSize = if (isCompact) 9.5.sp else 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = TextPure,
                        letterSpacing = if (isCompact) 1.sp else 2.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "PITWALL",
                    fontSize = if (isCompact) 36.sp else 46.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = TextPure,
                    letterSpacing = if (isCompact) 6.sp else 10.sp
                )

                Spacer(Modifier.height(if (isCompact) 28.dp else 40.dp))

                // F1 Starting Gantry (FIA Official 5-Column High-Tech Structure)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GlassSurfaceElevated,
                    border = BorderStroke(1.5.dp, GlassBorderActive),
                    modifier = Modifier.padding(horizontal = if (isCompact) 6.dp else 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = gantryPadH, vertical = if (isCompact) 18.dp else 24.dp)
                    ) {
                        // Top Gantry Mast Label
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            Text("FIA START SYSTEM", fontSize = if (isCompact) 8.sp else 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = TextMuted)
                            Text("5-LIGHT OPTICAL MATRIX", fontSize = if (isCompact) 8.sp else 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = TextMuted)
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(lightSpacing),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 1..5) {
                                val isRedOn = stage in 1..5 && i <= stage
                                val isGreenOn = stage == 6

                                val lightColor = when {
                                    isGreenOn -> TelemetryGreen
                                    isRedOn -> F1Red
                                    else -> Color(0xFF151821)
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    // Primary Main High-Output Lens
                                    Box(
                                        modifier = Modifier
                                            .size(lightSize)
                                            .clip(CircleShape)
                                            .background(lightColor)
                                            .border(
                                                width = if (isCompact) 2.dp else 2.5.dp,
                                                color = if (isRedOn || isGreenOn) lightColor else TextMuted.copy(alpha = 0.25f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Inner Lens Fresnel Ring
                                        Box(
                                            modifier = Modifier
                                                .size(innerFresnelSize)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isRedOn || isGreenOn) Color.White.copy(alpha = 0.35f)
                                                    else Color.Black.copy(alpha = 0.5f)
                                                )
                                        )
                                    }

                                    // Secondary Backup LED
                                    Box(
                                        modifier = Modifier
                                            .size(secondaryLedSize)
                                            .clip(CircleShape)
                                            .background(if (isRedOn || isGreenOn) lightColor else Color(0xFF0F1118))
                                            .border(1.dp, if (isRedOn || isGreenOn) lightColor else TextMuted.copy(alpha = 0.2f), CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                Text(
                    text = when (stage) {
                        0 -> "PREPARING START GANTRY..."
                        in 1..5 -> "LIGHTS ON... $stage"
                        6 -> "LIGHTS OUT & AWAY WE GO!"
                        else -> "PITWALL TELEMETRY ONLINE"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = if (stage == 6) TelemetryGreen else TextSilver,
                    letterSpacing = 2.sp
                )

                Spacer(Modifier.height(56.dp))

                // F1 Formula Car Driving Away Across Screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.offset { IntOffset(carTranslationX.value.roundToInt(), 0) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = org.jetbrains.compose.resources.painterResource(formulatrackr.shared.generated.resources.Res.drawable.ic_monochrome),
                            contentDescription = "F1 Car Logo",
                            tint = if (stage == 6) F1Red else TextPure,
                            modifier = Modifier.size(92.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .width(220.dp)
                                .height(5.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            F1Red.copy(alpha = if (stage == 6) 0.95f else 0.25f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
