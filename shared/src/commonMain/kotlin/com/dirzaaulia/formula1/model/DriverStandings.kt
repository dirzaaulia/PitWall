package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class DriverStandings(
    val classificationId: Int = 0,
    val driverId: String = "",
    val teamId: String = "",
    val position: Int = 0,
    val points: Double = 0.0,
    val wins: Int = 0,
    val driver: Driver = Driver(),
    val team: Team = Team()
)
