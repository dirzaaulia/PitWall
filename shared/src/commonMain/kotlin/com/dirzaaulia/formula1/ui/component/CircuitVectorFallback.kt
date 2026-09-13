package com.dirzaaulia.formula1.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import com.dirzaaulia.formula1.theme.F1Red
import com.dirzaaulia.formula1.theme.HairlineBorder
import com.dirzaaulia.formula1.theme.MonoWhite
import com.dirzaaulia.formula1.theme.ObsidianVoid
import com.dirzaaulia.formula1.util.CircuitPathRepository
import com.dirzaaulia.formula1.util.getCircuitSlug
import kotlin.math.min

@Composable
fun CircuitVectorFallback(
    circuitSlug: String? = null,
    circuitUrl: String = "",
    isLoading: Boolean = false,
    borderless: Boolean = false,
    showGrid: Boolean = false,
    accentColor: Color = F1Red,
    modifier: Modifier = Modifier
) {
    val resolvedSlug = remember(circuitSlug, circuitUrl) {
        when {
            !circuitSlug.isNullOrBlank() -> getCircuitSlug(circuitSlug)
            circuitUrl.isNotBlank() -> circuitUrl.substringAfterLast("/").substringBeforeLast(".").ifBlank { "monza-7" }
            else -> "monza-7"
        }
    }

    val circuitPaths = remember(resolvedSlug) {
        CircuitPathRepository.getPreloadedPaths(resolvedSlug)
            ?: CircuitPathRepository.getPreloadedPaths("monza-7")
    }

    val parsedRawTrack = remember(circuitPaths?.trackPathData) {
        circuitPaths?.trackPathData?.let {
            try {
                PathParser().parsePathString(it).toPath()
            } catch (_: Throwable) {
                null
            }
        }
    }

    val parsedRawSf = remember(circuitPaths?.startFinishPathData) {
        circuitPaths?.startFinishPathData?.let {
            try {
                PathParser().parsePathString(it).toPath()
            } catch (_: Throwable) {
                null
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "carMotion")
    val carProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "carProgress"
    )

    val boxModifier = if (borderless) {
        modifier
    } else {
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianVoid)
            .border(1.dp, HairlineBorder, RoundedCornerShape(14.dp))
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (showGrid && !borderless) {
                val gridSpacing = 24.dp.toPx()
                for (x in 0..size.width.toInt() step gridSpacing.toInt()) {
                    drawLine(
                        color = HairlineBorder.copy(alpha = 0.25f),
                        start = Offset(x.toFloat(), 0f),
                        end = Offset(x.toFloat(), size.height),
                        strokeWidth = 1f
                    )
                }
                for (y in 0..size.height.toInt() step gridSpacing.toInt()) {
                    drawLine(
                        color = HairlineBorder.copy(alpha = 0.25f),
                        start = Offset(0f, y.toFloat()),
                        end = Offset(size.width, y.toFloat()),
                        strokeWidth = 1f
                    )
                }
            }

            val track = parsedRawTrack ?: return@Canvas
            val bounds = track.getBounds()
            if (bounds.width <= 0 || bounds.height <= 0) return@Canvas

            val padding = if (borderless) 16.dp.toPx() else 28.dp.toPx()
            val availableW = size.width - padding * 2
            val availableH = size.height - padding * 2
            val scale = min(availableW / bounds.width, availableH / bounds.height)

            val dX = (size.width - bounds.width * scale) / 2f - bounds.left * scale
            val dY = (size.height - bounds.height * scale) / 2f - bounds.top * scale

            val transformMatrix = Matrix().apply {
                translate(dX, dY)
                scale(scale, scale)
            }

            val transformedTrack = Path().apply {
                addPath(track)
                transform(transformMatrix)
            }

            // Glow underlay
            drawPath(
                path = transformedTrack,
                color = accentColor.copy(alpha = 0.22f),
                style = Stroke(width = if (borderless) 8.dp.toPx() else 12.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Track asphalt outline
            drawPath(
                path = transformedTrack,
                color = if (borderless) Color(0x55FFFFFF) else Color(0x38FFFFFF),
                style = Stroke(width = if (borderless) 4.dp.toPx() else 6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Track racing line (crisp high contrast)
            drawPath(
                path = transformedTrack,
                color = MonoWhite,
                style = Stroke(width = if (borderless) 2.dp.toPx() else 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Start / Finish Line
            parsedRawSf?.let { rawSf ->
                val transformedSf = Path().apply {
                    addPath(rawSf)
                    transform(transformMatrix)
                }
                drawPath(path = transformedSf, color = Color.White, style = Fill)
                drawPath(path = transformedSf, color = accentColor, style = Stroke(width = 2.dp.toPx()))
            }

            // Animated car telemetry tracker dot
            val pathMeasure = PathMeasure().apply { setPath(transformedTrack, false) }
            if (pathMeasure.length > 0f) {
                val carPos = pathMeasure.getPosition(pathMeasure.length * carProgress)
                if (carPos != Offset.Unspecified) {
                    drawCircle(color = accentColor.copy(alpha = 0.4f), radius = 8.dp.toPx(), center = carPos)
                    drawCircle(color = accentColor, radius = 4.5.dp.toPx(), center = carPos)
                    drawCircle(color = MonoWhite, radius = 2.2.dp.toPx(), center = carPos)
                }
            }
        }
    }
}

