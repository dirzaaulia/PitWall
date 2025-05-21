package com.dirzaaulia.formula1.model.response

import com.dirzaaulia.formula1.model.Race
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val season: String,
    val races: List<Race>
)