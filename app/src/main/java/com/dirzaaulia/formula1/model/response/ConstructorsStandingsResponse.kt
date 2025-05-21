package com.dirzaaulia.formula1.model.response

import com.dirzaaulia.formula1.model.ConstructorsStandings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConstructorsStandingsResponse(
    @SerialName("constructors_championship")
    val constructorsStandings: List<ConstructorsStandings>
)