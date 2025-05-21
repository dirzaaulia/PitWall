package com.dirzaaulia.formula1.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Race

@Serializable
data class RaceDetail(
    //Json
    val season: String,
    val race: String,
    val grandPrixDate: String,
    val grandPrixWeekendStart: String
)

@Serializable
data class RaceResult(
    val year: Int,
    val round: Int,
    val city: String,
    //Json
    val resultType: String,
)

@Serializable
object Standings

@Serializable
object AppInfo