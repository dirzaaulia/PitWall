package com.dirzaaulia.formula1.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Team(
    val teamId: String = "",
    val teamName: String = "",
    val powerUnit: String = "V6 Turbo Hybrid",
    val base: String = "Formula 1 Base",
    val nationality: String? = null,
    val firstAppareance: Int? = null,
    val constructorsChampionships: Int? = null,
    val driversChampionships: Int? = null,
    @Transient val color: Color = Color(0xFFE10600),
    val drivers: List<String> = emptyList(),
    val points: Double = 0.0,
    val wins: Int = 0
)
