package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class ConstructorsStandings(
    val classificationId: Int = 0,
    val teamId: String = "",
    val position: Int = 0,
    val points: Double = 0.0,
    val wins: Int = 0,
    val team: Team = Team()
)
