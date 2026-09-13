package com.dirzaaulia.formula1.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenF1Session(
    @SerialName("session_key") val sessionKey: Int = 0,
    @SerialName("session_name") val sessionName: String = "",
    @SerialName("session_type") val sessionType: String = "",
    @SerialName("date_start") val dateStart: String = "",
    @SerialName("date_end") val dateEnd: String = "",
    @SerialName("meeting_key") val meetingKey: Int = 0,
    @SerialName("circuit_key") val circuitKey: Int = 0,
    @SerialName("circuit_short_name") val circuitShortName: String = "",
    @SerialName("country_name") val countryName: String = "",
    val year: Int = 0
)

@Serializable
data class OpenF1Driver(
    @SerialName("driver_number") val driverNumber: Int = 0,
    @SerialName("broadcast_name") val broadcastName: String = "",
    @SerialName("full_name") val fullName: String = "",
    @SerialName("name_acronym") val nameAcronym: String = "",
    @SerialName("team_name") val teamName: String = "",
    @SerialName("team_colour") val teamColour: String = "",
    @SerialName("headshot_url") val headshotUrl: String? = null
)

@Serializable
data class OpenF1Interval(
    val date: String = "",
    @SerialName("driver_number") val driverNumber: Int = 0,
    val interval: Double? = null,
    @SerialName("gap_to_leader") val gapToLeader: Double? = null
)

@Serializable
data class OpenF1Position(
    val date: String = "",
    @SerialName("driver_number") val driverNumber: Int = 0,
    val position: Int = 0
)

@Serializable
data class OpenF1Lap(
    @SerialName("driver_number") val driverNumber: Int = 0,
    @SerialName("lap_number") val lapNumber: Int = 0,
    @SerialName("lap_duration") val lapDuration: Double? = null,
    @SerialName("duration_sector_1") val durationSector1: Double? = null,
    @SerialName("duration_sector_2") val durationSector2: Double? = null,
    @SerialName("duration_sector_3") val durationSector3: Double? = null,
    @SerialName("is_pit_out_lap") val isPitOutLap: Boolean = false,
    @SerialName("st_speed") val stSpeed: Int? = null
)

@Serializable
data class OpenF1Stint(
    @SerialName("driver_number") val driverNumber: Int = 0,
    @SerialName("stint_number") val stintNumber: Int = 0,
    @SerialName("lap_start") val lapStart: Int = 1,
    @SerialName("lap_end") val lapEnd: Int = 1,
    val compound: String = "UNKNOWN",
    @SerialName("tyre_age_at_start") val tyreAgeAtStart: Int = 0
)

@Serializable
data class OpenF1RaceControl(
    val date: String = "",
    val category: String = "Other",
    val flag: String? = null,
    val scope: String? = null,
    val sector: Int? = null,
    val message: String = "",
    @SerialName("driver_number") val driverNumber: Int? = null,
    @SerialName("lap_number") val lapNumber: Int? = null
)

@Serializable
data class OpenF1Weather(
    val date: String = "",
    @SerialName("air_temperature") val airTemperature: Double = 26.5,
    @SerialName("track_temperature") val trackTemperature: Double = 42.0,
    val humidity: Double = 45.0,
    val rainfall: Int = 0,
    @SerialName("wind_speed") val windSpeed: Double = 2.4,
    @SerialName("wind_direction") val windDirection: Int = 180,
    val pressure: Double = 1013.2
)

@Serializable
data class OpenF1TeamRadio(
    val date: String = "",
    @SerialName("driver_number") val driverNumber: Int = 0,
    @SerialName("recording_url") val recordingUrl: String = "",
    @SerialName("lap_number") val lapNumber: Int? = null,
    val message: String = ""
)

data class LiveTimingDriverState(
    val position: Int,
    val driverNumber: Int,
    val code: String,
    val fullName: String,
    val teamName: String,
    val teamColor: Color,
    val gapToLeader: String,
    val intervalAhead: String,
    val compound: String,
    val tyreAge: Int,
    val currentLap: Int,
    val lastLapTime: String,
    val s1Time: String,
    val s1Status: SectorStatus = SectorStatus.NONE,
    val s2Time: String,
    val s2Status: SectorStatus = SectorStatus.NONE,
    val s3Time: String,
    val s3Status: SectorStatus = SectorStatus.NONE,
    val inPit: Boolean = false,
    val isOutLap: Boolean = false,
    val isRetired: Boolean = false,
    val positionChange: Int = 0, // > 0 gained, < 0 lost
    val trackProgress: Float = 0f, // 0.0 to 1.0 along the circuit path
    val bestLapTime: String = "",
    val pitStops: Int = 1,
    val speedTrapKmH: Int = 340
)

enum class SectorStatus {
    NONE,
    NORMAL,
    PERSONAL_BEST,
    OVERALL_FASTEST
}

data class CircuitSvgPaths(
    val trackPathData: String,
    val startFinishPathData: String? = null,
    val directionPathData: String? = null
)
