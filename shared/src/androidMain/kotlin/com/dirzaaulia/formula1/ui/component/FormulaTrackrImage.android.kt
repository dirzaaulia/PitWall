package com.dirzaaulia.formula1.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.svg.SvgDecoder
import com.dirzaaulia.formula1.theme.GlassSurface
import com.dirzaaulia.formula1.theme.GlassSurfaceElevated
import com.dirzaaulia.formula1.util.RemoveWhiteBackgroundTransformation

@Composable
fun Modifier.shimmerLoading(): Modifier {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            GlassSurface,
            GlassSurfaceElevated,
            GlassSurface
        ),
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    return this.background(brush)
}

@Composable
actual fun FormulaTrackrImage(
    url: String,
    contentDescription: String?,
    removeWhiteBackground: Boolean,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val context = LocalContext.current
    val model = ImageRequest.Builder(context)
        .data(url)
        .decoderFactory(SvgDecoder.Factory())
        .apply {
            if (removeWhiteBackground) {
                transformations(RemoveWhiteBackgroundTransformation())
            }
        }
        .build()

    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(modifier = Modifier.fillMaxSize().shimmerLoading())
        }
    )
}
