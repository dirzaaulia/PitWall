package com.dirzaaulia.formula1.model.response

import com.dirzaaulia.formula1.model.RaceResult
import kotlinx.serialization.Serializable

@Serializable
data class RaceResultResponse(
    val races: RaceResultData
)

@Serializable
data class RaceResultData(
    val results: List<RaceResult> = emptyList(),
    val qualyResults: List<RaceResult> = emptyList(),
    val fp1Results: List<RaceResult> = emptyList(),
    val fp2Results: List<RaceResult> = emptyList(),
    val fp3Results: List<RaceResult> = emptyList(),
    val sprintQualyResults: List<RaceResult> = emptyList(),
    val sprintRaceResults: List<RaceResult> = emptyList()
)