package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class ConstructorsStandings(
    val classificationId: Int,
    val teamId: String,
    val points: Double,
    val position: Int,
    val wins: Int? = null,
    val team: Team
)