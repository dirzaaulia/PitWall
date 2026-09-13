package com.dirzaaulia.formula1.ui.screen.telemetry

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirzaaulia.formula1.model.CircuitSvgPaths
import com.dirzaaulia.formula1.model.LiveTimingDriverState
import com.dirzaaulia.formula1.network.OpenF1Service
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.F1RedSubtle
import com.dirzaaulia.formula1.theme.GlassBorderActive
import com.dirzaaulia.formula1.theme.GlassSurface
import com.dirzaaulia.formula1.theme.MonoSilver
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianSurfaceElevated
import com.dirzaaulia.formula1.theme.PitchBlack
import com.dirzaaulia.formula1.util.CircuitPathRepository
import kotlin.math.min

@Composable
fun CircuitTracker(
    circuitSlug: String,
    circuitName: String,
    drivers: List<LiveTimingDriverState>,
    smoothProgression: Float = 0f,
    selectedDriverNumber: Int? = null,
    carPositionsMap: Map<Int, Float>? = null,
    height: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    // Synchronously check preloaded paths to avoid any race condition or flashing the wrong circuit outline
    var circuitPaths by remember(circuitSlug) {
        mutableStateOf<CircuitSvgPaths?>(CircuitPathRepository.getPreloadedPaths(circuitSlug))
    }
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(circuitSlug) {
        if (circuitPaths == null) {
            val fetched = OpenF1Service.getCircuitSvgPaths(circuitSlug)
            if (fetched != null && fetched.trackPathData.isNotBlank()) {
                circuitPaths = fetched
            } else {
                circuitPaths = CircuitPathRepository.getPreloadedPaths("monza-7")
            }
        }
    }

    val activePaths = circuitPaths

    val parsedRawTrack = remember(activePaths?.trackPathData) {
        activePaths?.trackPathData?.let {
            try {
                PathParser().parsePathString(it).toPath()
            } catch (_: Throwable) {
                null
            }
        }
    }

    val parsedRawSf = remember(activePaths?.startFinishPathData) {
        activePaths?.startFinishPathData?.let {
            try {
                PathParser().parsePathString(it).toPath()
            } catch (_: Throwable) {
                null
            }
        }
    }

    var cachedSfRatio by remember(circuitSlug) { mutableFloatStateOf(-1f) }

    val cardModifier = if (height > 0.dp) {
        modifier.fillMaxWidth().height(height)
    } else {
        modifier.fillMaxSize()
    }

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GlassSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(GlassBorderActive, Color(0x11FFFFFF))))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (activePaths == null || parsedRawTrack == null) {
                // Loading state: do NOT show a different track outline
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = F1Red, modifier = Modifier.size(32.dp))
                }
            } else {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val rawTrackPath = parsedRawTrack
                    val bounds = rawTrackPath.getBounds()
                    if (bounds.width <= 0 || bounds.height <= 0) return@Canvas

                    val paddingPx = 36.dp.toPx()
                    val scaleX = (size.width - paddingPx * 2) / bounds.width
                    val scaleY = (size.height - paddingPx * 2) / bounds.height
                    val scale = min(scaleX, scaleY)

                    val dx = (size.width - bounds.width * scale) / 2f - bounds.left * scale
                    val dy = (size.height - bounds.height * scale) / 2f - bounds.top * scale

                    val matrix = Matrix().apply {
                        translate(dx, dy)
                        scale(scale, scale)
                    }

                    // 1. Transform Track Path
                    val trackPath = Path().apply {
                        addPath(rawTrackPath)
                        transform(matrix)
                    }

                    // 2. Transform Start/Finish Line Path from SVG
                    val sfPath = parsedRawSf?.let { rawSf ->
                        Path().apply {
                            addPath(rawSf)
                            transform(matrix)
                        }
                    }

                    // 3. Draw outer glowing track halo
                    drawPath(
                        path = trackPath,
                        color = F1RedSubtle.copy(alpha = 0.5f),
                        style = Stroke(width = 22.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // 4. Draw tarmac track base
                    drawPath(
                        path = trackPath,
                        color = ObsidianSurfaceElevated,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // 5. Draw racing line guideline
                    drawPath(
                        path = trackPath,
                        color = MonoSilver.copy(alpha = 0.45f),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    val pathMeasure = PathMeasure()
                    pathMeasure.setPath(trackPath, false)
                    val pathLength = pathMeasure.length
                    if (pathLength <= 0) return@Canvas

                    // 6. Draw Official SVG Start/Finish Line & Align Distance
                    var sfTrackDistance = 0f
                    if (sfPath != null) {
                        drawPath(
                            path = sfPath,
                            color = Color.White,
                            style = Fill
                        )
                        drawPath(
                            path = sfPath,
                            color = Color(0xFFE10600),
                            style = Stroke(width = 1.5.dp.toPx())
                        )

                        // Calibrate track distance ratio once per circuit
                        if (cachedSfRatio < 0f) {
                            val sfBounds = sfPath.getBounds()
                            val sfCenter = sfBounds.center

                            var minDistanceSq = Float.MAX_VALUE
                            val steps = 60
                            var bestDist = 0f
                            for (i in 0..steps) {
                                val testDist = (i.toFloat() / steps.toFloat()) * pathLength
                                val testPos = pathMeasure.getPosition(testDist)
                                if (testPos != Offset.Unspecified) {
                                    val dX = testPos.x - sfCenter.x
                                    val dY = testPos.y - sfCenter.y
                                    val distSq = dX * dX + dY * dY
                                    if (distSq < minDistanceSq) {
                                        minDistanceSq = distSq
                                        bestDist = testDist
                                    }
                                }
                            }
                            cachedSfRatio = (bestDist / pathLength).coerceIn(0f, 1f)
                        }
                        sfTrackDistance = cachedSfRatio * pathLength
                    }

                    // 7. Draw Cars with Continuous Dynamic Progress Matching the Timing Tower Exactly
                    val sortedDrivers = drivers.sortedByDescending { it.position }
                    sortedDrivers.forEach { driver ->
                        if (driver.isRetired) return@forEach

                        val carLapProgress = carPositionsMap?.get(driver.driverNumber) ?: run {
                            if (driver.currentLap == 1 && smoothProgression < 0.12f) {
                                val gridDistFraction = (0.008f + (driver.position - 1) * 0.0025f).coerceAtMost(0.06f)
                                val launchFade = (smoothProgression / 0.12f).coerceIn(0f, 1f)
                                val effProg = driver.trackProgress - (gridDistFraction * (1f - launchFade))
                                ((effProg % 1.0f) + 1.0f) % 1.0f
                            } else {
                                driver.trackProgress
                            }
                        }
                        if (carLapProgress < 0f) return@forEach

                        val distance = (sfTrackDistance + carLapProgress * pathLength) % pathLength

                        val pos = pathMeasure.getPosition(distance)
                        val tangent = pathMeasure.getTangent(distance)

                        if (pos != Offset.Unspecified) {
                            val normal = if (tangent != Offset.Unspecified) {
                                Offset(-tangent.y, tangent.x)
                            } else Offset.Zero

                            val isGridOdd = driver.position % 2 != 0
                            val lateralOffsetPx = if (driver.currentLap == 1 && smoothProgression < 0.04f) {
                                if (isGridOdd) 6.dp.toPx() else -6.dp.toPx()
                            } else {
                                if (isGridOdd) 4.dp.toPx() else -4.dp.toPx()
                            }
                            val carCenter = pos + normal * lateralOffsetPx

                            val isLeader = driver.position == 1
                            val isSelected = selectedDriverNumber == driver.driverNumber
                            val carRadius = when {
                                isSelected -> 10.dp.toPx()
                                isLeader -> 9.dp.toPx()
                                else -> 7.dp.toPx()
                            }

                            // If selected, draw animated targeting reticle
                            if (isSelected) {
                                drawCircle(
                                    color = F1Red,
                                    radius = carRadius * 2.8f,
                                    center = carCenter,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawCircle(
                                    color = F1Red.copy(alpha = 0.35f),
                                    radius = carRadius * 2.2f,
                                    center = carCenter
                                )
                            } else {
                                drawCircle(
                                    color = driver.teamColor.copy(alpha = 0.4f),
                                    radius = carRadius * 2.2f,
                                    center = carCenter
                                )
                            }

                            drawCircle(
                                color = if (isLeader) Color(0xFFFFD700) else driver.teamColor,
                                radius = carRadius,
                                center = carCenter
                            )

                            drawCircle(
                                color = PitchBlack,
                                radius = carRadius * 0.55f,
                                center = carCenter
                            )

                            val label = if (driver.code.isNotBlank()) driver.code else driver.driverNumber.toString()
                            val textLayout = textMeasurer.measure(
                                text = label,
                                style = TextStyle(
                                    color = MonoWhite,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )

                            val textOffset = Offset(
                                x = carCenter.x + carRadius + 4.dp.toPx(),
                                y = carCenter.y - textLayout.size.height / 2f
                            )

                            drawRoundRect(
                                color = PitchBlack.copy(alpha = 0.85f),
                                topLeft = textOffset - Offset(2.dp.toPx(), 2.dp.toPx()),
                                size = androidx.compose.ui.geometry.Size(
                                    textLayout.size.width + 4.dp.toPx(),
                                    textLayout.size.height + 4.dp.toPx()
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )

                            drawText(
                                textLayoutResult = textLayout,
                                topLeft = textOffset
                            )
                        }
                    }
                }
            }

            // Top Left Circuit Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PitchBlack.copy(alpha = 0.85f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = circuitName.ifBlank { "CIRCUIT VIEW" }.uppercase(),
                    color = MonoSilver,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
