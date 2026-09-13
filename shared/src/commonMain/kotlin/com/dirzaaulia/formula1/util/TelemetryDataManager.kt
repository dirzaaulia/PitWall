package com.dirzaaulia.formula1.util

import androidx.compose.ui.graphics.Color
import com.dirzaaulia.formula1.model.LiveTimingDriverState
import com.dirzaaulia.formula1.model.OpenF1Driver
import com.dirzaaulia.formula1.model.OpenF1Lap
import com.dirzaaulia.formula1.model.OpenF1RaceControl
import com.dirzaaulia.formula1.model.OpenF1Stint
import com.dirzaaulia.formula1.model.OpenF1TeamRadio
import com.dirzaaulia.formula1.model.OpenF1Weather
import com.dirzaaulia.formula1.model.SectorStatus
import com.dirzaaulia.formula1.network.JolpicaNetworkService
import com.dirzaaulia.formula1.network.OpenF1Service

data class ArchiveRaceOption(
    val sessionKey: Int,
    val round: Int,
    val name: String,
    val circuitShortName: String,
    val country: String,
    val flag: String,
    val circuitSlug: String,
    val totalLaps: Int,
    val trackLengthKm: String
)

object TelemetryDataManager {
    val LIVE_2026_RACE = ArchiveRaceOption(11364, 14, "Spanish Grand Prix", "Madrid", "Spain", "🇪🇸", "madring-1", 66, "5.474 KM")

    val OFFICIAL_2026_RACES = listOf(
        ArchiveRaceOption(11361, 13, "Italian Grand Prix", "Monza", "Italy", "🇮🇹", "monza-7", 53, "5.793 KM"),
        ArchiveRaceOption(11326, 12, "British Grand Prix", "Silverstone", "United Kingdom", "🇬🇧", "silverstone-8", 52, "5.891 KM"),
        ArchiveRaceOption(11334, 11, "Belgian Grand Prix", "Spa-Francorchamps", "Belgium", "🇧🇪", "spa-francorchamps-4", 44, "7.004 KM"),
        ArchiveRaceOption(11307, 10, "Spanish Grand Prix", "Catalunya", "Spain", "🇪🇸", "catalunya-6", 66, "4.675 KM"),
        ArchiveRaceOption(11299, 8, "Monaco Grand Prix", "Monte Carlo", "Monaco", "🇲🇨", "monaco-6", 78, "3.337 KM"),
        ArchiveRaceOption(11234, 1, "Australian Grand Prix", "Melbourne", "Australia", "🇦🇺", "melbourne-2", 58, "5.278 KM"),
        ArchiveRaceOption(11261, 4, "Bahrain Grand Prix", "Sakhir", "Bahrain", "🇧🇭", "bahrain-1", 57, "5.412 KM"),
        ArchiveRaceOption(11269, 5, "Saudi Arabian GP", "Jeddah", "Saudi Arabia", "🇸🇦", "jeddah-1", 50, "6.174 KM"),
        ArchiveRaceOption(11280, 6, "Miami Grand Prix", "Miami", "United States", "🇺🇸", "miami-1", 57, "5.412 KM"),
        ArchiveRaceOption(11315, 9, "Austrian Grand Prix", "Spielberg", "Austria", "🇦🇹", "spielberg-3", 71, "4.318 KM"),
        ArchiveRaceOption(11342, 12, "Hungarian Grand Prix", "Hungaroring", "Hungary", "🇭🇺", "hungaroring-3", 70, "4.381 KM"),
        ArchiveRaceOption(11353, 13, "Dutch Grand Prix", "Zandvoort", "Netherlands", "🇳🇱", "zandvoort-5", 72, "4.259 KM")
    )

    // Complete Official 2026 Formula 1 Grid (11 Teams, 22 Drivers)
    val OFFICIAL_2026_DRIVERS = listOf(
        OpenF1Driver(12, "K ANTONELLI", "Andrea Kimi Antonelli", "ANT", "Mercedes", "00D7B6", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/A/ANDANT01_Andrea%20Kimi_Antonelli/andant01.png.transform/2col-retina/image.png"),
        OpenF1Driver(63, "G RUSSELL", "George Russell", "RUS", "Mercedes", "00D7B6", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/G/GEORUS01_George_Russell/georus01.png.transform/2col-retina/image.png"),
        OpenF1Driver(3, "M VERSTAPPEN", "Max Verstappen", "VER", "Red Bull Racing", "4781D7", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/M/MAXVER01_Max_Verstappen/maxver01.png.transform/2col-retina/image.png"),
        OpenF1Driver(1, "L NORRIS", "Lando Norris", "NOR", "McLaren", "F47600", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/L/LANNOR01_Lando_Norris/lannor01.png.transform/2col-retina/image.png"),
        OpenF1Driver(81, "O PIASTRI", "Oscar Piastri", "PIA", "McLaren", "F47600", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/O/OSCPIA01_Oscar_Piastri/oscpia01.png.transform/2col-retina/image.png"),
        OpenF1Driver(44, "L HAMILTON", "Lewis Hamilton", "HAM", "Ferrari", "ED1131", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/L/LEWHAM01_Lewis_Hamilton/lewham01.png.transform/2col-retina/image.png"),
        OpenF1Driver(10, "P GASLY", "Pierre Gasly", "GAS", "Alpine", "00A1E8", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/P/PIEGAS01_Pierre_Gasly/piegas01.png.transform/2col-retina/image.png"),
        OpenF1Driver(41, "A LINDBLAD", "Arvid Lindblad", "LIN", "Racing Bulls", "6C98FF", "https://media.formula1.com/image/upload/c_fill,g_face,w_412,h_412/v1740000001/common/f1/2026/racingbulls/arvlin01/2026racingbullsarvlin01front.webp"),
        OpenF1Driver(43, "F COLAPINTO", "Franco Colapinto", "COL", "Alpine", "00A1E8", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/F/FRACOL01_Franco_Colapinto/fracol01.png.transform/2col-retina/image.png"),
        OpenF1Driver(22, "Y TSUNODA", "Yuki Tsunoda", "TSU", "Racing Bulls", "6C98FF", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/Y/YUKTSU01_Yuki_Tsunoda/yuktsu01.png.transform/2col-retina/image.png"),
        OpenF1Driver(5, "G BORTOLETO", "Gabriel Bortoleto", "BOR", "Audi", "F50537", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/G/GABBOR01_Gabriel_Bortoleto/gabbor01.png.transform/2col-retina/image.png"),
        OpenF1Driver(27, "N HULKENBERG", "Nico Hulkenberg", "HUL", "Audi", "F50537", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/N/NICHUL01_Nico_Hulkenberg/nichul01.png.transform/2col-retina/image.png"),
        OpenF1Driver(55, "C SAINZ", "Carlos Sainz", "SAI", "Williams", "1868DB", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/C/CARSAI01_Carlos_Sainz/carsai01.png.transform/2col-retina/image.png"),
        OpenF1Driver(30, "L LAWSON", "Liam Lawson", "LAW", "Red Bull Racing", "4781D7", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/L/LIALAW01_Liam_Lawson/lialaw01.png.transform/2col-retina/image.png"),
        OpenF1Driver(87, "O BEARMAN", "Oliver Bearman", "BEA", "Haas F1 Team", "9C9FA2", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/O/OLIBEA01_Oliver_Bearman/olibea01.png.transform/2col-retina/image.png"),
        OpenF1Driver(31, "E OCON", "Esteban Ocon", "OCO", "Haas F1 Team", "9C9FA2", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/E/ESTOCO01_Esteban_Ocon/estoco01.png.transform/2col-retina/image.png"),
        OpenF1Driver(23, "A ALBON", "Alexander Albon", "ALB", "Williams", "1868DB", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/A/ALEALB01_Alexander_Albon/alealb01.png.transform/2col-retina/image.png"),
        OpenF1Driver(11, "S PEREZ", "Sergio Perez", "PER", "Cadillac", "909090", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/S/SERPER01_Sergio_Perez/serper01.png.transform/2col-retina/image.png"),
        OpenF1Driver(77, "V BOTTAS", "Valtteri Bottas", "BOT", "Cadillac", "909090", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/V/VALBOT01_Valtteri_Bottas/valbot01.png.transform/2col-retina/image.png"),
        OpenF1Driver(18, "L STROLL", "Lance Stroll", "STR", "Aston Martin", "229971", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/L/LANSTR01_Lance_Stroll/lanstr01.png.transform/2col-retina/image.png"),
        OpenF1Driver(14, "F ALONSO", "Fernando Alonso", "ALO", "Aston Martin", "229971", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/F/FERALO01_Fernando_Alonso/feralo01.png.transform/2col-retina/image.png"),
        OpenF1Driver(16, "C LECLERC", "Charles Leclerc", "LEC", "Ferrari", "ED1131", "https://media.formula1.com/d_driver_fallback_image.png/content/dam/fom-website/drivers/C/CHALEC01_Charles_Leclerc/chalec01.png.transform/2col-retina/image.png")
    )

    val PRELOADED_RACE_CONTROL: List<OpenF1RaceControl> = listOf(
        OpenF1RaceControl(category = "Flag", flag = "GREEN", scope = "Track", message = "TRACK CLEAR - GREEN FLAG", lapNumber = 1),
        OpenF1RaceControl(category = "CarEvent", driverNumber = 27, message = "CAR 27 (HUL) UNDER INVESTIGATION - TURN 1 INCIDENT", lapNumber = 1),
        OpenF1RaceControl(category = "Drs", message = "DRS ENABLED - ZONES 1 & 2 ACTIVE", lapNumber = 2),
        OpenF1RaceControl(category = "Flag", flag = "YELLOW", scope = "Sector 1", sector = 1, message = "YELLOW FLAG SECTOR 1", lapNumber = 7),
        OpenF1RaceControl(category = "CarEvent", driverNumber = 22, message = "CAR 22 (TSU) PIT LANE RETIREMENT - SIDEPOD DAMAGE", lapNumber = 7),
        OpenF1RaceControl(category = "Flag", flag = "CLEAR", scope = "Sector 1", message = "TRACK CLEAR - ALL SECTORS GREEN", lapNumber = 8),
        OpenF1RaceControl(category = "CarEvent", driverNumber = 27, message = "CAR 27 (HUL) 5 SECOND TIME PENALTY - CAUSING A COLLISION", lapNumber = 12),
        OpenF1RaceControl(category = "Other", message = "PIT STOP WINDOW OPEN FOR FRONT RUNNERS", lapNumber = 15),
        OpenF1RaceControl(category = "CarEvent", driverNumber = 20, message = "CAR 20 (MAG) 10 SECOND PENALTY - INCIDENT WITH CAR 10", lapNumber = 19),
        OpenF1RaceControl(category = "Other", message = "TRACK SURFACE DRY - AMBIENT PEAK 28.5°C", lapNumber = 26),
        OpenF1RaceControl(category = "CarEvent", driverNumber = 44, message = "CAR 44 (HAM) BLACK & WHITE FLAG - TRACK LIMITS TURN 11", lapNumber = 34),
        OpenF1RaceControl(category = "Other", message = "LEAD BATTLE: LEC VS PIA GAP 2.4 SECONDS", lapNumber = 42),
        OpenF1RaceControl(category = "Other", message = "FINAL 5 LAPS - TYRE MANAGEMENT ONGOING", lapNumber = 48),
        OpenF1RaceControl(category = "Flag", flag = "CHEQUERED", scope = "Track", message = "CHEQUERED FLAG - CHARLES LECLERC WINS ITALIAN GP", lapNumber = 53)
    )

    val PRELOADED_TEAM_RADIO: List<OpenF1TeamRadio> = listOf(
        OpenF1TeamRadio(driverNumber = 16, lapNumber = 1, message = "Good launch, tyres in optimal window", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/CARSAI01_55.mp3"),
        OpenF1TeamRadio(driverNumber = 4, lapNumber = 6, message = "Pace is strong, closing into DRS range", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/LANNOR01_4.mp3"),
        OpenF1TeamRadio(driverNumber = 1, lapNumber = 14, message = "Front tyres graining, understeer mid-corner", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/MAXVER01_1.mp3"),
        OpenF1TeamRadio(driverNumber = 44, lapNumber = 20, message = "Box this lap, box box. Confirm hards", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/CARSAI01_55.mp3"),
        OpenF1TeamRadio(driverNumber = 81, lapNumber = 33, message = "Push now Oscar, we have free air ahead", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/LANNOR01_4.mp3"),
        OpenF1TeamRadio(driverNumber = 16, lapNumber = 45, message = "Tyres are holding up well, gap is stable", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/CARSAI01_55.mp3"),
        OpenF1TeamRadio(driverNumber = 16, lapNumber = 53, message = "Mamma mia! Yes! Forza Ferrari!", recordingUrl = "https://livetiming.formula1.com/static/TeamRadio/CARSAI01_55.mp3")
    )

    fun getRaceControlForLap(
        allMessages: List<OpenF1RaceControl>,
        currentLap: Int,
        totalLaps: Int = 53
    ): List<OpenF1RaceControl> {
        val baseList = if (allMessages.isNotEmpty()) allMessages else PRELOADED_RACE_CONTROL
        return baseList.filter { msg ->
            val msgLap = msg.lapNumber ?: run {
                val idx = baseList.indexOf(msg)
                (((idx + 1).toFloat() / baseList.size) * totalLaps).toInt().coerceIn(1, totalLaps)
            }
            msgLap <= currentLap
        }
    }

    fun getTeamRadioForLap(
        radioList: List<OpenF1TeamRadio>,
        currentLap: Int,
        totalLaps: Int = 53
    ): List<OpenF1TeamRadio> {
        val baseList = if (radioList.isNotEmpty()) radioList else PRELOADED_TEAM_RADIO
        return baseList.filter { radio ->
            val rLap = radio.lapNumber ?: run {
                val idx = baseList.indexOf(radio)
                (((idx + 1).toFloat() / baseList.size) * totalLaps).toInt().coerceIn(1, totalLaps)
            }
            rLap <= currentLap
        }
    }

    fun getWeatherForLap(
        weatherList: List<OpenF1Weather>,
        currentLap: Int,
        totalLaps: Int = 53
    ): OpenF1Weather {
        if (weatherList.isNotEmpty()) {
            val idx = (((currentLap - 1).toFloat() / (totalLaps - 1).coerceAtLeast(1)) * (weatherList.size - 1)).toInt().coerceIn(0, weatherList.size - 1)
            return weatherList[idx]
        }
        val lapRatio = ((currentLap - 1).toFloat() / (totalLaps - 1).coerceAtLeast(1)).coerceIn(0f, 1f)
        val air = 27.0 + 1.6 * kotlin.math.sin(lapRatio * kotlin.math.PI)
        val track = 43.8 - 4.3 * lapRatio
        val hum = 41.0 + 4.0 * lapRatio
        val wind = 2.2 + 1.2 * lapRatio
        val windDir = 180 + (30 * lapRatio).toInt()
        val press = 1013.4 - (0.9 * lapRatio)
        return OpenF1Weather(
            airTemperature = (air * 10).toInt() / 10.0,
            trackTemperature = (track * 10).toInt() / 10.0,
            humidity = (hum * 10).toInt() / 10.0,
            rainfall = 0,
            windSpeed = (wind * 10).toInt() / 10.0,
            windDirection = windDir,
            pressure = (press * 10).toInt() / 10.0
        )
    }

    fun getFlagForLap(
        activeRaceControl: List<OpenF1RaceControl>,
        currentLap: Int,
        totalLaps: Int = 53,
        subLapFraction: Float = 0.5f
    ): Pair<String, Color> {
        if (currentLap >= totalLaps && subLapFraction >= 0.90f) {
            return "CHEQUERED FLAG" to Color(0xFFFFFFFF)
        }
        val latestFlagRc = activeRaceControl.lastOrNull { !it.flag.isNullOrBlank() }
        val flag = latestFlagRc?.flag?.uppercase()
        return when {
            flag == "CHEQUERED" -> "CHEQUERED FLAG" to Color(0xFFFFFFFF)
            flag == "RED" -> "RED FLAG" to Color(0xFFE10600)
            flag == "YELLOW" -> "YELLOW FLAG" to Color(0xFFFFD700)
            flag == "SC" || (latestFlagRc?.category?.contains("SafetyCar", ignoreCase = true) == true && flag != "CLEAR") -> "SAFETY CAR" to Color(0xFFFF9100)
            flag == "VSC" -> "VIRTUAL SC" to Color(0xFFFF9100)
            currentLap == 1 && subLapFraction < 0.08f -> "START PROCEDURE" to Color(0xFFFFD700)
            else -> "GREEN FLAG" to Color(0xFF00E676)
        }
    }

    fun getPreloadedLaps(sessionKey: Int): List<OpenF1Lap> {
        return if (sessionKey == 11361) MonzaPreloadedData.LAPS else emptyList()
    }

    fun getPreloadedStints(sessionKey: Int): List<OpenF1Stint> {
        return if (sessionKey == 11361) MonzaPreloadedData.STINTS else emptyList()
    }

    fun computeTimingStandings(
        currentLap: Int,
        subLapFraction: Float,
        totalLaps: Int = 53,
        driversList: List<OpenF1Driver> = emptyList(),
        lapsList: List<OpenF1Lap> = emptyList(),
        stintsList: List<OpenF1Stint> = emptyList()
    ): List<LiveTimingDriverState> {
        val drivers = if (driversList.isNotEmpty()) driversList else OFFICIAL_2026_DRIVERS
        val driverMap = drivers.associateBy { it.driverNumber }

        val activeLaps = if (lapsList.isNotEmpty()) lapsList else MonzaPreloadedData.LAPS
        val activeStints = if (stintsList.isNotEmpty()) stintsList else MonzaPreloadedData.STINTS

        val driverLaps = activeLaps.groupBy { it.driverNumber }
        val driverMaxLap = driverLaps.mapValues { (_, laps) -> laps.maxOfOrNull { it.lapNumber } ?: 0 }

        val gridOrder = MonzaPreloadedData.STARTING_GRID
        val gridIndex = gridOrder.withIndex().associate { it.value to it.index }

        data class RunningCalc(
            val driverNumber: Int,
            val totalTimeSec: Double,
            val isDnf: Boolean,
            val dnfLap: Int?,
            val isLapped: Boolean,
            val lapsCompleted: Int,
            val currentLapData: OpenF1Lap?,
            val nextLapData: OpenF1Lap?
        )

        val calcs = drivers.map { driver ->
            val dNum = driver.driverNumber
            val dLaps = driverLaps[dNum].orEmpty().associateBy { it.lapNumber }
            val maxL = driverMaxLap[dNum] ?: 0

            val isDnf = maxL < currentLap && maxL < (totalLaps - 3)
            val dnfLap = if (isDnf) maxL else null

            val totalTimeSec = if (isDnf) {
                999999.0 + (100 - maxL)
            } else if (currentLap == 1 && subLapFraction == 0f) {
                // Precise Starting Grid Position
                val gPos = gridIndex[dNum] ?: 10
                gPos * 0.15
            } else {
                // Sum completed laps up to currentLap - 1
                var cumTime = 0.0
                val upToLap = minOf(currentLap - 1, maxL)
                for (i in 1..upToLap) {
                    cumTime += dLaps[i]?.lapDuration ?: 85.0
                }
                // Continuous in-lap elapsed time
                if (currentLap <= maxL) {
                    val curLapDur = dLaps[currentLap]?.lapDuration ?: 85.0
                    cumTime += (subLapFraction.toDouble() * curLapDur)
                    // Blend out starting grid delta across lap 1
                    if (currentLap == 1) {
                        val gPos = gridIndex[dNum] ?: 10
                        val gridWeight = (1.0 - subLapFraction.toDouble()).coerceIn(0.0, 1.0)
                        cumTime += (gPos * 0.25 * gridWeight)
                    }
                }
                cumTime
            }

            val curLapData = dLaps[currentLap]
            val nextLapData = dLaps[currentLap + 1]
            val isLapped = currentLap >= (totalLaps - 1) && maxL < (totalLaps - 1) && !isDnf

            RunningCalc(
                driverNumber = dNum,
                totalTimeSec = totalTimeSec,
                isDnf = isDnf,
                dnfLap = dnfLap,
                isLapped = isLapped,
                lapsCompleted = minOf(currentLap, maxL),
                currentLapData = curLapData,
                nextLapData = nextLapData
            )
        }

        val prevCalcs = drivers.map { driver ->
            val dNum = driver.driverNumber
            val dLaps = driverLaps[dNum].orEmpty().associateBy { it.lapNumber }
            val maxL = driverMaxLap[dNum] ?: 0
            val isDnf = maxL < currentLap && maxL < (totalLaps - 3)
            val t = if (isDnf) {
                999999.0 + (100 - maxL)
            } else if (currentLap == 1) {
                val gPos = gridIndex[dNum] ?: 10
                gPos * 0.15
            } else {
                var cum = 0.0
                for (i in 1 until currentLap) {
                    cum += dLaps[i]?.lapDuration ?: 85.0
                }
                cum
            }
            dNum to t
        }.sortedBy { it.second }.mapIndexed { idx, pair -> pair.first to (idx + 1) }.toMap()

        val sortedCalcs = calcs.sortedWith(
            compareBy<RunningCalc> { it.isDnf }
                .thenBy { it.isLapped }
                .thenBy { it.totalTimeSec }
        )

        val leaderTime = sortedCalcs.firstOrNull { !it.isDnf }?.totalTimeSec ?: 0.0

        return sortedCalcs.mapIndexed { index, calc ->
            val pos = index + 1
            val driver = driverMap[calc.driverNumber] ?: OpenF1Driver(calc.driverNumber)
            val prevPos = prevCalcs[calc.driverNumber] ?: pos
            val posChange = prevPos - pos

            val prevTime = if (index > 0) sortedCalcs[index - 1].totalTimeSec else leaderTime
            val gapToLeaderSec = (calc.totalTimeSec - leaderTime).coerceAtLeast(0.0)
            val intervalAheadSec = (calc.totalTimeSec - prevTime).coerceAtLeast(0.0)

            val gapStr = when {
                calc.isDnf -> "DNF (L${calc.dnfLap})"
                pos == 1 -> "LEADER"
                calc.isLapped -> "+${totalLaps - calc.lapsCompleted} LAP(S)"
                else -> "+${formatSeconds(gapToLeaderSec)}"
            }

            val intStr = when {
                calc.isDnf -> "--"
                pos == 1 -> "--"
                calc.isLapped -> "--"
                else -> "+${formatSeconds(intervalAheadSec)}"
            }

            val dStints = activeStints.filter { it.driverNumber == calc.driverNumber }
            val activeStint = dStints.firstOrNull { currentLap >= it.lapStart && currentLap <= it.lapEnd }
                ?: dStints.maxByOrNull { it.stintNumber }

            val compound = activeStint?.compound?.uppercase() ?: if (index % 2 == 0) "MEDIUM" else "HARD"
            val stintStart = activeStint?.lapStart ?: 1
            val tyreAge = (currentLap - stintStart + (activeStint?.tyreAgeAtStart ?: 0)).coerceAtLeast(1)

            val lastLapSec = calc.currentLapData?.lapDuration ?: 84.5
            val lastLapStr = formatLapDuration(lastLapSec)

            val s1 = calc.currentLapData?.durationSector1 ?: 27.5
            val s2 = calc.currentLapData?.durationSector2 ?: 28.5
            val s3 = calc.currentLapData?.durationSector3 ?: 28.5

            val s1Status = when {
                s1 <= 27.2 -> SectorStatus.OVERALL_FASTEST
                s1 <= 27.5 -> SectorStatus.PERSONAL_BEST
                else -> SectorStatus.NORMAL
            }
            val s2Status = when {
                s2 <= 28.2 -> SectorStatus.OVERALL_FASTEST
                s2 <= 28.5 -> SectorStatus.PERSONAL_BEST
                else -> SectorStatus.NORMAL
            }
            val s3Status = when {
                s3 <= 28.3 -> SectorStatus.OVERALL_FASTEST
                s3 <= 28.7 -> SectorStatus.PERSONAL_BEST
                else -> SectorStatus.NORMAL
            }

            // Continuous Dynamic Track Geometry Offset (0.0 to 1.0)
            val lapEst = (calc.currentLapData?.lapDuration ?: 84.0).coerceAtLeast(60.0)
            val gapFraction = (gapToLeaderSec / lapEst).toFloat()
            val trackProg = if (calc.isDnf) {
                -1f
            } else {
                ((subLapFraction - gapFraction) % 1.0f + 1.0f) % 1.0f
            }

            val teamColor = if (driver.teamColour.isNotBlank()) {
                OpenF1Service.parseColorHex(driver.teamColour, JolpicaNetworkService.getTeamColor(driver.teamName))
            } else {
                JolpicaNetworkService.getTeamColor(driver.teamName)
            }

            val inPit = calc.currentLapData?.isPitOutLap == true || (activeStint != null && currentLap == activeStint.lapStart && subLapFraction < 0.15f)

            LiveTimingDriverState(
                position = pos,
                driverNumber = calc.driverNumber,
                code = driver.nameAcronym.ifBlank { driver.broadcastName.take(3).uppercase() },
                fullName = driver.fullName.ifBlank { driver.broadcastName },
                teamName = driver.teamName,
                teamColor = teamColor,
                gapToLeader = gapStr,
                intervalAhead = intStr,
                compound = compound,
                tyreAge = tyreAge,
                currentLap = currentLap,
                lastLapTime = lastLapStr,
                s1Time = formatSeconds(s1),
                s1Status = s1Status,
                s2Time = formatSeconds(s2),
                s2Status = s2Status,
                s3Time = formatSeconds(s3),
                s3Status = s3Status,
                inPit = inPit,
                isOutLap = calc.currentLapData?.isPitOutLap == true,
                isRetired = calc.isDnf,
                positionChange = posChange,
                trackProgress = trackProg,
                bestLapTime = formatLapDuration(calc.currentLapData?.lapDuration ?: (83.5 + (pos * 0.09))),
                pitStops = (activeStint?.stintNumber ?: 1) - 1,
                speedTrapKmH = 346 - (pos * 2) + ((calc.driverNumber * 7) % 12)
            )
        }
    }

    /**
     * Dedicated continuous multi-lap progression calculation for the Circuit Radar.
     * Chained smoothly across laps so cars continuously progress, catch up, and overtake
     * naturally at 60 FPS without resetting or jumping when a lap finishes.
     */
    fun computeContinuousCircuitTrackProgress(
        raceProgressFloat: Float,
        totalLaps: Int = 53,
        driversList: List<OpenF1Driver> = emptyList(),
        lapsList: List<OpenF1Lap> = emptyList()
    ): Map<Int, Float> {
        val drivers = if (driversList.isNotEmpty()) driversList else OFFICIAL_2026_DRIVERS
        val activeLaps = if (lapsList.isNotEmpty()) lapsList else MonzaPreloadedData.LAPS

        val driverLaps = activeLaps.groupBy { it.driverNumber }
        val driverMaxLap = driverLaps.mapValues { (_, laps) -> laps.maxOfOrNull { it.lapNumber } ?: 0 }

        val gridOrder = MonzaPreloadedData.STARTING_GRID
        val gridIndex = gridOrder.withIndex().associate { it.value to it.index }

        // Leader reference: driver #10 on lap 1, then based on real race progression
        val leaderNum = 10
        val leaderLaps = driverLaps[leaderNum].orEmpty().associateBy { it.lapNumber }
        val leaderLapIndex = raceProgressFloat.toInt().coerceIn(0, totalLaps - 1)
        val leaderLapFraction = (raceProgressFloat - leaderLapIndex).coerceIn(0f, 1f)

        var leaderElapsedSec = 0.0
        for (i in 1..leaderLapIndex) {
            leaderElapsedSec += leaderLaps[i]?.lapDuration ?: 85.0
        }
        val curLeaderLapDur = leaderLaps[leaderLapIndex + 1]?.lapDuration ?: 85.0
        leaderElapsedSec += (leaderLapFraction.toDouble() * curLeaderLapDur)

        return drivers.associate { driver ->
            val dNum = driver.driverNumber
            val dLaps = driverLaps[dNum].orEmpty().associateBy { it.lapNumber }
            val maxL = driverMaxLap[dNum] ?: 0

            val isDnf = maxL < (raceProgressFloat.toInt() + 1) && maxL < (totalLaps - 3)
            if (isDnf) {
                dNum to -1f // off-track
            } else {
                var cum = 0.0
                var lapFound = 1
                var inLapFraction = 0.0

                for (l in 1..minOf(totalLaps, maxL)) {
                    val dur = dLaps[l]?.lapDuration ?: 85.0
                    if (cum + dur > leaderElapsedSec) {
                        lapFound = l
                        inLapFraction = ((leaderElapsedSec - cum) / dur).coerceIn(0.0, 1.0)
                        break
                    }
                    cum += dur
                    lapFound = l
                    inLapFraction = 1.0
                }

                val globalProg = (lapFound - 1).toDouble() + inLapFraction

                // Starting grid launch: cars start in staggered grid slots behind the finish line
                // and advance monotonically forward across the start/finish line down into turn 1.
                val gPos = gridIndex[dNum] ?: 10
                val gridDistBehind = (0.008f + (gPos * 0.0025f)).coerceAtMost(0.06f)

                val effectiveProg = if (globalProg < 0.12) {
                    val launchFade = (globalProg / 0.12).toFloat().coerceIn(0f, 1f)
                    val currentOffset = gridDistBehind * (1.0f - launchFade)
                    globalProg.toFloat() - currentOffset
                } else {
                    globalProg.toFloat()
                }

                val circuitProgress = ((effectiveProg % 1.0f) + 1.0f) % 1.0f
                dNum to circuitProgress
            }
        }
    }

    private fun formatLapDuration(sec: Double): String {
        val mins = (sec / 60).toInt()
        val remainder = sec - (mins * 60)
        val remSec = remainder.toInt()
        val millis = ((remainder - remSec) * 1000).toInt().coerceIn(0, 999)
        return "$mins:${if (remSec < 10) "0$remSec" else "$remSec"}.${millis.toString().padStart(3, '0')}"
    }

    private fun formatSeconds(sec: Double): String {
        val whole = sec.toInt()
        val millis = ((sec - whole) * 1000).toInt().coerceIn(0, 999)
        return "$whole.${millis.toString().padStart(3, '0')}"
    }
}
