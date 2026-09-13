package com.dirzaaulia.formula1.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
actual fun FormulaTrackrImage(
    url: String,
    contentDescription: String?,
    removeWhiteBackground: Boolean,
    modifier: Modifier,
    contentScale: ContentScale
) {
    WasmAsyncImage(
        url = url,
        contentDescription = contentDescription,
        removeWhiteBackground = removeWhiteBackground,
        modifier = modifier,
        contentScale = contentScale
    )
}
