package com.dirzaaulia.formula1.model

import androidx.compose.ui.graphics.Color

data class PodiumResult(
    val position: Int,
    val code: String,
    val name: String,
    val team: String,
    val teamColor: Color,
    val timeOrStatus: String
)
