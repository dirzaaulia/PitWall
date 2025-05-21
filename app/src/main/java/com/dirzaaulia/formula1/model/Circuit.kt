package com.dirzaaulia.formula1.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Circuit(
    val country: String,
    val circuitName: String,
    val city: String,
    val circuitLength: String,
    val lapRecord: String,
    val fastestLapDriverId: String,
    val fastestLapYear: Int,
    @SerialName("firstParticipationYear")
    val firstYear: Int,
    val corners: Int,
    val url: String
    
) {
    companion object {
        val default = Circuit(
            country = "",
            circuitName = "",
            city = "",
            circuitLength = "",
            lapRecord = "",
            fastestLapDriverId = "",
            url = "",
            fastestLapYear = 0,
            firstYear = 0,
            corners = 0,
        )
    }
}