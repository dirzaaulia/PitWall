package com.dirzaaulia.formula1.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TelemetryRow(
    val position: Int = 0,
    val number: Int = 0,
    val code: String = "",
    val name: String = "",
    val team: String = "",
    @Transient val teamColor: Color = Color(0xFFE10600),
    val gapToLeader: String = "--",
    val interval: String = "--",
    val s1Time: String = "--",
    @Transient val s1Status: Color = Color(0xFF30D158),
    val s2Time: String = "--",
    @Transient val s2Status: Color = Color(0xFF30D158),
    val s3Time: String = "--",
    @Transient val s3Status: Color = Color(0xFF30D158),
    val lastLap: String = "--",
    val speedTrap: Int = 0,
    val tyreCompound: String = "M",
    val tyreAge: Int = 0,
    val pitCount: Int = 0,
    val positionDelta: Int = 0
)
