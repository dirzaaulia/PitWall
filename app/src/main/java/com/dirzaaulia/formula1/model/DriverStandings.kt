package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverStandings(
    val classificationId: Int,
    val driverId: String,
    val teamId: String,
    val points: Double,
    val position: Int,
    val wins: Int? = null,
    val driver:  Driver,
    val team: Team
)