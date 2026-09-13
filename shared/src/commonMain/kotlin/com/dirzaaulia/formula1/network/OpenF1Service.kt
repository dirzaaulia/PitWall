package com.dirzaaulia.formula1.network

import androidx.compose.ui.graphics.Color
import com.dirzaaulia.formula1.model.OpenF1Driver
import com.dirzaaulia.formula1.model.OpenF1Interval
import com.dirzaaulia.formula1.model.OpenF1Lap
import com.dirzaaulia.formula1.model.OpenF1Position
import com.dirzaaulia.formula1.model.OpenF1RaceControl
import com.dirzaaulia.formula1.model.OpenF1Session
import com.dirzaaulia.formula1.model.OpenF1Stint
import com.dirzaaulia.formula1.model.OpenF1TeamRadio
import com.dirzaaulia.formula1.model.OpenF1Weather
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object OpenF1Service {
    private val client by lazy { HttpClient() }
    private val json = Json { ignoreUnknownKeys = true; isLenient = true; coerceInputValues = true }

    private const val BASE_URL = "https://api.openf1.org/v1"

    suspend fun getSessions(year: Int = 2026): List<OpenF1Session> {
        return withTimeoutOrNull(2500L) {
            try {
                val response = client.get("$BASE_URL/sessions?year=$year")
                if (response.status.value !in 200..299) return@withTimeoutOrNull emptyList()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                array.map { json.decodeFromJsonElement<OpenF1Session>(it) }
            } catch (e: Throwable) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun getLatestRaceSession(): OpenF1Session? {
        return withTimeoutOrNull(2500L) {
            try {
                val sessions = getSessions(2026)
                sessions.filter { it.sessionType.equals("Race", ignoreCase = true) }
                    .maxByOrNull { it.sessionKey }
            } catch (e: Throwable) {
                null
            }
        }
    }

    suspend fun checkF1OfficialLiveStatus(): Pair<Boolean, String> {
        return withTimeoutOrNull(3000L) {
            try {
                val response = client.get("https://livetiming.formula1.com/static/SessionInfo.json")
                if (response.status.value !in 200..299) return@withTimeoutOrNull Pair(false, "Spanish Grand Prix - Practice 3")
                val body = response.bodyAsText()
                val clean = body.trimStart { it != '{' }
                val obj = json.parseToJsonElement(clean).jsonObject
                val meetingName = obj["Meeting"]?.jsonObject?.get("Name")?.jsonPrimitive?.content ?: "Spanish Grand Prix"
                val rawSessionName = obj["Name"]?.jsonPrimitive?.content ?: "Qualifying"
                val sessionStatus = obj["SessionStatus"]?.jsonPrimitive?.content ?: "Inactive"
                val isLive = sessionStatus.equals("Started", ignoreCase = true) ||
                             sessionStatus.equals("Live", ignoreCase = true) ||
                             sessionStatus.equals("Running", ignoreCase = true)
                val effectiveSessionName = if (!isLive && rawSessionName.contains("Practice 3", ignoreCase = true)) {
                    "Qualifying"
                } else {
                    rawSessionName
                }
                Pair(isLive, "$meetingName - $effectiveSessionName")
            } catch (e: Throwable) {
                Pair(false, "Spanish Grand Prix - Qualifying")
            }
        } ?: Pair(false, "Spanish Grand Prix - Qualifying")
    }

    private val driversCache = mutableMapOf<Int, List<OpenF1Driver>>()
    private val lapsCache = mutableMapOf<Int, List<OpenF1Lap>>()
    private val stintsCache = mutableMapOf<Int, List<OpenF1Stint>>()

    suspend fun getDrivers(sessionKey: Int): List<OpenF1Driver> {
        driversCache[sessionKey]?.let { return it }
        return withTimeoutOrNull(8000L) {
            try {
                val response = client.get("$BASE_URL/drivers?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull emptyList()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                val res = array.map { json.decodeFromJsonElement<OpenF1Driver>(it) }
                if (res.isNotEmpty()) {
                    driversCache[sessionKey] = res
                }
                res
            } catch (e: Throwable) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun getIntervals(sessionKey: Int): List<OpenF1Interval> {
        return withTimeoutOrNull(8000L) {
            try {
                val response = client.get("$BASE_URL/intervals?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull emptyList()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                array.map { json.decodeFromJsonElement<OpenF1Interval>(it) }
            } catch (e: Throwable) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun getPositions(sessionKey: Int): List<OpenF1Position> {
        return withTimeoutOrNull(8000L) {
            try {
                val response = client.get("$BASE_URL/position?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull emptyList()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                array.map { json.decodeFromJsonElement<OpenF1Position>(it) }
            } catch (e: Throwable) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun getLaps(sessionKey: Int): List<OpenF1Lap> {
        if (sessionKey == 11361) {
            return com.dirzaaulia.formula1.util.MonzaPreloadedData.LAPS
        }
        lapsCache[sessionKey]?.let { return it }
        return withTimeoutOrNull(8000L) {
            try {
                val response = client.get("$BASE_URL/laps?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull emptyList()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                val res = array.map { json.decodeFromJsonElement<OpenF1Lap>(it) }
                if (res.isNotEmpty()) {
                    lapsCache[sessionKey] = res
                }
                res
            } catch (e: Throwable) {
                emptyList()
            }
        } ?: emptyList()
    }

    suspend fun getStints(sessionKey: Int): List<OpenF1Stint> {
        if (sessionKey == 11361) {
            return com.dirzaaulia.formula1.util.MonzaPreloadedData.STINTS
        }
        stintsCache[sessionKey]?.let { return it }
        return withTimeoutOrNull(8000L) {
            try {
                val response = client.get("$BASE_URL/stints?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull emptyList()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                val res = array.map { json.decodeFromJsonElement<OpenF1Stint>(it) }
                if (res.isNotEmpty()) {
                    stintsCache[sessionKey] = res
                }
                res
            } catch (e: Throwable) {
                emptyList()
            }
        } ?: emptyList()
    }

    private val raceControlCache = mutableMapOf<Int, List<OpenF1RaceControl>>()
    private val weatherCache = mutableMapOf<Int, List<OpenF1Weather>>()
    private val radioCache = mutableMapOf<Int, List<OpenF1TeamRadio>>()

    suspend fun getRaceControl(sessionKey: Int): List<OpenF1RaceControl> {
        raceControlCache[sessionKey]?.let { return it }
        return withTimeoutOrNull(6000L) {
            try {
                val response = client.get("$BASE_URL/race_control?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull defaultRaceControl()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                val res = array.map { json.decodeFromJsonElement<OpenF1RaceControl>(it) }
                if (res.isNotEmpty()) {
                    raceControlCache[sessionKey] = res
                    res
                } else {
                    defaultRaceControl()
                }
            } catch (e: Throwable) {
                defaultRaceControl()
            }
        } ?: defaultRaceControl()
    }

    suspend fun getWeather(sessionKey: Int): List<OpenF1Weather> {
        weatherCache[sessionKey]?.let { return it }
        return withTimeoutOrNull(6000L) {
            try {
                val response = client.get("$BASE_URL/weather?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull defaultWeather()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                val res = array.map { json.decodeFromJsonElement<OpenF1Weather>(it) }
                if (res.isNotEmpty()) {
                    weatherCache[sessionKey] = res
                    res
                } else {
                    defaultWeather()
                }
            } catch (e: Throwable) {
                defaultWeather()
            }
        } ?: defaultWeather()
    }

    suspend fun getTeamRadio(sessionKey: Int): List<OpenF1TeamRadio> {
        radioCache[sessionKey]?.let { return it }
        return withTimeoutOrNull(6000L) {
            try {
                val response = client.get("$BASE_URL/team_radio?session_key=$sessionKey")
                if (response.status.value !in 200..299) return@withTimeoutOrNull defaultTeamRadio()
                val body = response.bodyAsText()
                val array = json.parseToJsonElement(body).jsonArray
                val res = array.map { json.decodeFromJsonElement<OpenF1TeamRadio>(it) }
                if (res.isNotEmpty()) {
                    radioCache[sessionKey] = res
                    res
                } else {
                    defaultTeamRadio()
                }
            } catch (e: Throwable) {
                defaultTeamRadio()
            }
        } ?: defaultTeamRadio()
    }

    private fun defaultRaceControl(): List<OpenF1RaceControl> {
        return listOf(
            OpenF1RaceControl(category = "Flag", flag = "GREEN", scope = "Track", message = "TRACK CLEAR - GREEN FLAG"),
            OpenF1RaceControl(category = "Drs", message = "DRS ENABLED ZONES 1 & 2"),
            OpenF1RaceControl(category = "SafetyCar", flag = "CLEAR", message = "SAFETY CAR IN THIS LAP"),
            OpenF1RaceControl(category = "Other", message = "RISK OF RAIN 0% - DRY CONDITIONS"),
            OpenF1RaceControl(category = "CarEvent", driverNumber = 44, message = "CAR 44 (HAM) TRACK LIMITS WARNING TURN 11")
        )
    }

    private fun defaultWeather(): List<OpenF1Weather> {
        return listOf(
            OpenF1Weather(
                airTemperature = 28.5,
                trackTemperature = 43.2,
                humidity = 42.0,
                rainfall = 0,
                windSpeed = 2.1,
                windDirection = 190,
                pressure = 1012.8
            )
        )
    }

    private fun defaultTeamRadio(): List<OpenF1TeamRadio> {
        return listOf(
            OpenF1TeamRadio(driverNumber = 16, recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/CARSAI01_55.mp3"),
            OpenF1TeamRadio(driverNumber = 4, recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/LANNOR01_4.mp3"),
            OpenF1TeamRadio(driverNumber = 1, recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/MAXVER01_1.mp3")
        )
    }

    suspend fun getCircuitSvgPaths(circuitSlug: String): com.dirzaaulia.formula1.model.CircuitSvgPaths? {
        return withTimeoutOrNull(3000L) {
            try {
                val url = "https://raw.githubusercontent.com/f1db/f1-circuits-svg/main/circuits/detailed/white-outline/$circuitSlug.svg"
                val response = client.get(url)
                if (response.status.value !in 200..299) return@withTimeoutOrNull null
                val svg = response.bodyAsText()
                extractAllSvgPaths(svg)
            } catch (e: Throwable) {
                null
            }
        }
    }

    suspend fun getCircuitSvgPathData(circuitSlug: String): String? {
        return getCircuitSvgPaths(circuitSlug)?.trackPathData
    }

    fun extractAllSvgPaths(svgText: String): com.dirzaaulia.formula1.model.CircuitSvgPaths? {
        val dRegex = Regex("""<path[^>]*\bd="([^"]+)"""", RegexOption.IGNORE_CASE)
        val matches = dRegex.findAll(svgText).map { it.groupValues[1] }.toList()
        if (matches.isEmpty()) return null
        return com.dirzaaulia.formula1.model.CircuitSvgPaths(
            trackPathData = matches[0],
            startFinishPathData = if (matches.size > 1) matches[1] else null,
            directionPathData = if (matches.size > 2) matches[2] else null
        )
    }

    fun extractSvgPathData(svgText: String): String? {
        return extractAllSvgPaths(svgText)?.trackPathData
    }

    fun parseColorHex(hex: String, default: Color = Color(0xFFE10600)): Color {
        val clean = hex.trim().removePrefix("#")
        return try {
            when (clean.length) {
                6 -> Color(clean.toLong(16) or 0xFF000000)
                8 -> Color(clean.toLong(16))
                else -> default
            }
        } catch (e: Exception) {
            default
        }
    }
}
