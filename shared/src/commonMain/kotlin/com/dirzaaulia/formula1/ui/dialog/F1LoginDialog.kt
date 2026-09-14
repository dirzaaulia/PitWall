package com.dirzaaulia.formula1.ui.dialog

import androidx.compose.runtime.Composable

@Composable
expect fun F1LoginDialog(
    onDismiss: () -> Unit,
    onTokenReceived: (String) -> Unit
)
