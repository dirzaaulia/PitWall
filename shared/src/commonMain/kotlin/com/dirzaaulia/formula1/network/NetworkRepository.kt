package com.dirzaaulia.formula1.network

import androidx.compose.ui.graphics.Color
import com.dirzaaulia.formula1.model.ConstructorsStandings
import com.dirzaaulia.formula1.model.DriverStandings
import com.dirzaaulia.formula1.model.PodiumResult
import com.dirzaaulia.formula1.model.Race
import com.dirzaaulia.formula1.model.TelemetryRow
import com.dirzaaulia.formula1.theme.TelemetryGreen
import com.dirzaaulia.formula1.theme.TelemetryPurple
import com.dirzaaulia.formula1.theme.TelemetryYellow

object NetworkRepository {

    suspend fun getRaces(season: Int = 2026): List<Race> {
        val (_, list) = JolpicaNetworkService.getSchedule(season.toString())
        if (list.isNotEmpty()) return list
        // Try fetching current season if requested future season is not yet released on Ergast
        return JolpicaNetworkService.getSchedule("current").second
    }

    suspend fun getSeasonResults(season: Int = 2026): Map<Int, List<PodiumResult>> {
        val map = JolpicaNetworkService.getSeasonResults(season.toString())
        if (map.isNotEmpty()) return map
        return JolpicaNetworkService.getSeasonResults("current")
    }

    suspend fun getGrandPrixResults(season: Int = 2026, round: Int, isSprint: Boolean = false): com.dirzaaulia.formula1.model.GrandPrixWeekendResults {
        return JolpicaNetworkService.getGrandPrixResults(season.toString(), round, isSprint)
    }

    suspend fun getDriverStandings(season: Int = 2026): List<DriverStandings> {
        val list = JolpicaNetworkService.getDriverStandings(season.toString())
        if (list.isNotEmpty()) return list
        return JolpicaNetworkService.getDriverStandings("current")
    }

    suspend fun getConstructorStandings(season: Int = 2026): List<ConstructorsStandings> {
        val list = JolpicaNetworkService.getConstructorStandings(season.toString())
        if (list.isNotEmpty()) return list
        return JolpicaNetworkService.getConstructorStandings("current")
    }

    fun getLiveTelemetryRows(): List<TelemetryRow> = listOf(
        TelemetryRow(1, 12, "ANT", "Andrea Kimi Antonelli", "Mercedes", Color(0xFF00D2BE), "LEADER", "--", "27.812", TelemetryPurple, "28.109", TelemetryPurple, "25.125", TelemetryGreen, "1:21.046", 342, "M", 16, 1, 0),
        TelemetryRow(2, 63, "RUS", "George Russell", "Mercedes", Color(0xFF00D2BE), "+1.238", "+1.238", "27.954", TelemetryGreen, "28.231", TelemetryYellow, "25.198", TelemetryGreen, "1:21.383", 340, "M", 16, 1, 0),
        TelemetryRow(3, 44, "HAM", "Lewis Hamilton", "Ferrari", Color(0xFFE8002D), "+2.891", "+1.653", "28.012", TelemetryYellow, "28.188", TelemetryGreen, "25.241", TelemetryYellow, "1:21.441", 344, "H", 24, 0, 1),
        TelemetryRow(4, 4, "NOR", "Lando Norris", "McLaren", Color(0xFFFF8000), "+3.450", "+0.559", "27.899", TelemetryGreen, "28.310", TelemetryYellow, "25.290", TelemetryYellow, "1:21.499", 339, "M", 14, 1, -1),
        TelemetryRow(5, 16, "LEC", "Charles Leclerc", "Ferrari", Color(0xFFE8002D), "+4.120", "+0.670", "28.102", TelemetryYellow, "28.245", TelemetryYellow, "25.312", TelemetryYellow, "1:21.659", 341, "H", 24, 0, 0),
        TelemetryRow(6, 1, "VER", "Max Verstappen", "Red Bull", Color(0xFF3671C6), "+6.782", "+2.662", "28.045", TelemetryYellow, "28.320", TelemetryYellow, "25.350", TelemetryYellow, "1:21.715", 343, "S", 8, 2, 2),
        TelemetryRow(7, 81, "PIA", "Oscar Piastri", "McLaren", Color(0xFFFF8000), "+8.120", "+1.338", "28.190", TelemetryYellow, "28.401", TelemetryYellow, "25.410", TelemetryYellow, "1:22.001", 338, "M", 15, 1, 0),
        TelemetryRow(8, 14, "ALO", "Fernando Alonso", "Aston Martin", Color(0xFF229971), "+12.450", "+4.330", "28.350", TelemetryYellow, "28.520", TelemetryYellow, "25.520", TelemetryYellow, "1:22.390", 336, "H", 28, 0, 0),
        TelemetryRow(9, 55, "SAI", "Carlos Sainz", "Williams", Color(0xFF64C4FF), "+15.890", "+3.440", "28.420", TelemetryYellow, "28.610", TelemetryYellow, "25.590", TelemetryYellow, "1:22.620", 337, "M", 18, 1, -1),
        TelemetryRow(10, 10, "GAS", "Pierre Gasly", "Alpine", Color(0xFFFF87BC), "+18.230", "+2.340", "28.510", TelemetryYellow, "28.690", TelemetryYellow, "25.680", TelemetryYellow, "1:22.880", 335, "M", 19, 1, 1),
        TelemetryRow(11, 6, "HAD", "Isack Hadjar", "Red Bull", Color(0xFF3671C6), "+21.450", "+3.220", "28.620", TelemetryYellow, "28.780", TelemetryYellow, "25.750", TelemetryYellow, "1:23.150", 334, "S", 12, 1, 0),
        TelemetryRow(12, 43, "COL", "Franco Colapinto", "Alpine", Color(0xFFFF87BC), "+24.890", "+3.440", "28.710", TelemetryYellow, "28.850", TelemetryYellow, "25.830", TelemetryYellow, "1:23.390", 332, "M", 17, 1, 1)
    )
}
