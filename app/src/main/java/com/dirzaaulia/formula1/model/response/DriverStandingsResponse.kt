package com.dirzaaulia.formula1.model.response

import com.dirzaaulia.formula1.model.DriverStandings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriverStandingsResponse(
    @SerialName("drivers_championship")
    val driversStandings: List<DriverStandings>
)