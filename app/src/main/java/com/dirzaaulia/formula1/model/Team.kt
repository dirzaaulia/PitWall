package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val teamId: String? = null,
    val teamName: String,
    val nationality: String? = null,
    val firstAppareance: Int? = null,
    val constructorsChampionships: Int?,
    val driversChampionships: Int?
)