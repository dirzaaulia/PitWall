package com.dirzaaulia.formula1.model

import androidx.compose.ui.graphics.Color

data class RaceClassificationRow(
    val position: Int,
    val driverNumber: Int,
    val driverCode: String,
    val driverName: String,
    val nationality: String,
    val teamName: String,
    val teamColor: Color,
    val timeOrGap: String,
    val laps: Int,
    val points: Int,
    val isFastestLap: Boolean = false,
    val fastestLapTime: String = "",
    val gridPosition: Int = position
)

data class QualifyingClassificationRow(
    val position: Int,
    val driverNumber: Int,
    val driverCode: String,
    val driverName: String,
    val teamName: String,
    val teamColor: Color,
    val q1: String,
    val q2: String,
    val q3: String,
    val gapToPole: String
)

data class PracticeClassificationRow(
    val position: Int,
    val driverNumber: Int,
    val driverCode: String,
    val driverName: String,
    val teamName: String,
    val teamColor: Color,
    val bestLap: String,
    val gapToLeader: String,
    val lapsCompleted: Int
)

data class SprintClassificationRow(
    val position: Int,
    val driverNumber: Int,
    val driverCode: String,
    val driverName: String,
    val teamName: String,
    val teamColor: Color,
    val timeOrGap: String,
    val points: Int
)

data class GrandPrixWeekendResults(
    val round: Int,
    val raceResults: List<RaceClassificationRow> = emptyList(),
    val qualifyingResults: List<QualifyingClassificationRow> = emptyList(),
    val sprintResults: List<SprintClassificationRow> = emptyList(),
    val fp1Results: List<PracticeClassificationRow> = emptyList(),
    val fp2Results: List<PracticeClassificationRow> = emptyList(),
    val fp3Results: List<PracticeClassificationRow> = emptyList()
)
