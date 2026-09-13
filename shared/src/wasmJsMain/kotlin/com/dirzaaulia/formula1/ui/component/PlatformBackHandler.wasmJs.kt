package com.dirzaaulia.formula1.ui.component

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // WASM back handler
}
