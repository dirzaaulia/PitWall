package com.dirzaaulia.formula1.util

import android.graphics.Bitmap
import coil3.size.Size
import coil3.transform.Transformation

class RemoveWhiteBackgroundTransformation(
    private val threshold: Int = 220
) : Transformation() {
    override val cacheKey: String = "RemoveWhiteBackgroundTransformation_$threshold"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val output = input.copy(Bitmap.Config.ARGB_8888, true)
        val width = output.width
        val height = output.height
        val pixels = IntArray(width * height)
        output.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            if (r >= threshold && g >= threshold && b >= threshold) {
                pixels[i] = 0
            }
        }

        output.setPixels(pixels, 0, width, 0, 0, width, height)
        return output
    }
}
