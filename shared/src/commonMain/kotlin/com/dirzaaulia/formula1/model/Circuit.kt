package com.dirzaaulia.formula1.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Circuit(
    val circuitId: String = "",
    val circuitName: String = "",
    val location: String = "",
    val country: String = "",
    val city: String = "",
    val circuitLength: String = "",
    val lapRecord: String = "",
    val fastestLapDriverId: String = "",
    val fastestLapYear: Int = 0,
    @SerialName("firstParticipationYear") val firstYear: Int = 0,
    val corners: Int = 0,
    val latitude: String = "",
    val longitude: String = "",
    val lat: String = "",
    val long: String = "",
    val drsZones: Int = 0,
    val laps: Int = 0,
    val raceDistance: String = "",
    val url: String = ""
) {
    companion object {
        val default = Circuit()
    }
}
