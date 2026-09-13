package com.dirzaaulia.formula1.wasm

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.dirzaaulia.formula1.FormulaTrackrApp
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val loading = document.getElementById("loading")
    loading?.remove()

    val body = document.body ?: return
    ComposeViewport(body) {
        FormulaTrackrApp()
    }
}
