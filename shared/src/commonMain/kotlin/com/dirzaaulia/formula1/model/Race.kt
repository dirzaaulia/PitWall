package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class Race(
    val raceId: String = "",
    val raceName: String = "",
    val round: Int = 0,
    val url: String = "",
    val circuit: Circuit = Circuit(),
    val date: String = "",
    val time: String = "",
    val isSprint: Boolean = false,
    val isDebut: Boolean = false,
    val totalLaps: Int = 58,
    val trackLength: String = "5.278 km",
    val condition: String = "DRY",
    val schedule: RaceSchedule? = null
) {
    companion object {
        val default = Race()
    }
}

@Serializable
data class RaceSchedule(
    val race: RaceDate? = null,
    val qualy: RaceDate? = null,
    val fp1: RaceDate? = null,
    val fp2: RaceDate? = null,
    val fp3: RaceDate? = null,
    val sprintQualy: RaceDate? = null,
    val sprintRace: RaceDate? = null
)

@Serializable
data class RaceDate(
    val date: String? = null,
    val time: String? = null
)
