package com.dirzaaulia.formula1.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
expect fun FormulaTrackrImage(
    url: String,
    contentDescription: String? = null,
    removeWhiteBackground: Boolean = false,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
)
