package com.dirzaaulia.formula1.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Image as SkiaImage

private val wasmHttpClient by lazy { HttpClient() }

@Composable
fun WasmAsyncImage(
    url: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = 1.0f,
    removeWhiteBackground: Boolean = false,
    threshold: Int = 220
) {
    var imageBitmap by remember(url, removeWhiteBackground) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(url) { mutableStateOf(true) }

    LaunchedEffect(url, removeWhiteBackground) {
        if (url.isBlank()) {
            imageBitmap = null
            isLoading = false
            return@LaunchedEffect
        }
        isLoading = true
        imageBitmap = try {
            val bytes = wasmHttpClient.get(url).readRawBytes()
            val skiaImage = SkiaImage.makeFromEncoded(bytes)
            if (removeWhiteBackground) {
                val tempBitmap = Bitmap()
                tempBitmap.allocN32Pixels(skiaImage.width, skiaImage.height)
                val tempCanvas = org.jetbrains.skia.Canvas(tempBitmap)
                tempCanvas.drawImage(skiaImage, 0f, 0f)
                val pixmap = tempBitmap.peekPixels()
                if (pixmap != null) {
                    val w = pixmap.info.width
                    val h = pixmap.info.height
                    val outBitmap = Bitmap()
                    outBitmap.allocN32Pixels(w, h)
                    val outCanvas = org.jetbrains.skia.Canvas(outBitmap)
                    val paint = Paint()
                    for (y in 0 until h) {
                        for (x in 0 until w) {
                            val c = pixmap.getColor(x, y)
                            val r = (c shr 16) and 0xFF
                            val g = (c shr 8) and 0xFF
                            val b = c and 0xFF
                            if (r < threshold || g < threshold || b < threshold) {
                                paint.color = c
                                outCanvas.drawRect(Rect.makeXYWH(x.toFloat(), y.toFloat(), 1f, 1f), paint)
                            }
                        }
                    }
                    SkiaImage.makeFromBitmap(outBitmap).toComposeImageBitmap()
                } else {
                    skiaImage.toComposeImageBitmap()
                }
            } else {
                skiaImage.toComposeImageBitmap()
            }
        } catch (e: Throwable) {
            null
        } finally {
            isLoading = false
        }
    }

    val bitmap = imageBitmap
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            contentScale = contentScale,
            alpha = alpha,
            modifier = modifier
        )
    }
}
