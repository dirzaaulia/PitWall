package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class Race(
    val raceId: String,
    val raceName: String,
    val round: Int,
    val url: String,
    val circuit: Circuit,
    val schedule: RaceSchedule? = null
) {
    companion object {
        val default = Race(
            raceId = "",
            raceName = "",
            url = "",
            round = 0,
            circuit = Circuit.default
        )
    }
}

@Serializable
data class RaceSchedule(
    val race: RaceDate,
    val qualy: RaceDate,
    val fp1: RaceDate,
    val fp2: RaceDate,
    val fp3: RaceDate,
    val sprintQualy: RaceDate,
    val sprintRace: RaceDate
)

@Serializable
data class RaceDate(
    val date: String? = null,
    val time: String? = null
)