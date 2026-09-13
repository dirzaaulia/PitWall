package com.dirzaaulia.formula1.util

import io.ktor.util.date.GMTDate

private const val F1_CDN = "https://media.formula1.com"

val CIRCUIT_MAP_KEYS = mapOf(
    "sepang" to "Malaysia", "malaysia" to "Malaysia", "malaysian" to "Malaysia",
    "spain" to "Spain", "spanish" to "Spain", "madring" to "Spain", "madrid" to "Spain", "barcelona" to "Spain", "catalunya" to "Spain",
    "australia" to "Australia", "australian" to "Australia", "albert park" to "Australia", "melbourne" to "Australia",
    "china" to "China", "chinese" to "China", "shanghai" to "China",
    "japan" to "Japan", "japanese" to "Japan", "suzuka" to "Japan",
    "bahrain" to "Bahrain", "sakhir" to "Bahrain",
    "saudi" to "Saudi_Arabia", "jeddah" to "Saudi_Arabia",
    "miami" to "Miami",
    "imola" to "Emilia_Romagna", "emilia" to "Emilia_Romagna", "romagna" to "Emilia_Romagna",
    "monaco" to "Monaco", "monte carlo" to "Monaco",
    "canada" to "Canada", "canadian" to "Canada", "montreal" to "Canada",
    "austria" to "Austria", "austrian" to "Austria", "spielberg" to "Austria", "red bull ring" to "Austria",
    "britain" to "Great_Britain", "british" to "Great_Britain", "silverstone" to "Great_Britain", "uk" to "Great_Britain",
    "hungary" to "Hungary", "hungarian" to "Hungary", "hungaroring" to "Hungary",
    "belgium" to "Belgium", "belgian" to "Belgium", "spa" to "Belgium",
    "netherlands" to "Netherlands", "dutch" to "Netherlands", "zandvoort" to "Netherlands",
    "italy" to "Italy", "italian" to "Italy", "monza" to "Italy",
    "azerbaijan" to "Azerbaijan", "baku" to "Azerbaijan",
    "singapore" to "Singapore",
    "austin" to "USA", "united states" to "USA", "cota" to "USA", "americas" to "USA",
    "mexico" to "Mexico", "mexican" to "Mexico", "hermanos" to "Mexico",
    "brazil" to "Brazil", "brazilian" to "Brazil", "interlagos" to "Brazil", "sao paulo" to "Brazil",
    "vegas" to "Las_Vegas", "las vegas" to "Las_Vegas",
    "qatar" to "Qatar", "lusail" to "Qatar",
    "abu dhabi" to "Abu_Dhabi", "yas marina" to "Abu_Dhabi", "uae" to "Abu_Dhabi"
)

fun getDriverHeadshotUrl(codeOrId: String): String {
    val lower = codeOrId.lowercase().trim()
    val base = "$F1_CDN/d_driver_fallback_image.png/content/dam/fom-website/drivers"
    return when {
        lower.contains("ant") || lower.contains("aka") || lower.contains("antonelli") || lower.contains("kimi") -> "$base/A/ANDANT01_Andrea%20Kimi_Antonelli/andant01.png.transform/2col-retina/image.png"
        lower.contains("lin") || lower.contains("lindblad") || lower.contains("arvid") -> "https://media.formula1.com/image/upload/c_fill,g_face,w_412,h_412/v1740000001/common/f1/2026/racingbulls/arvlin01/2026racingbullsarvlin01front.webp"
        lower.contains("alb") || lower.contains("albon") -> "$base/A/ALEALB01_Alexander_Albon/alealb01.png.transform/2col-retina/image.png"
        lower.contains("bor") || lower.contains("bortoleto") -> "$base/G/GABBOR01_Gabriel_Bortoleto/gabbor01.png.transform/2col-retina/image.png"
        lower.contains("had") || lower.contains("hadjar") -> "$base/I/ISAHAD01_Isack_Hadjar/isahad01.png.transform/2col-retina/image.png"
        lower.contains("ver") || lower.contains("verstappen") -> "$base/M/MAXVER01_Max_Verstappen/maxver01.png.transform/2col-retina/image.png"
        lower.contains("nor") || lower.contains("norris") -> "$base/L/LANNOR01_Lando_Norris/lannor01.png.transform/2col-retina/image.png"
        lower.contains("lec") || lower.contains("leclerc") -> "$base/C/CHALEC01_Charles_Leclerc/chalec01.png.transform/2col-retina/image.png"
        lower.contains("pia") || lower.contains("piastri") -> "$base/O/OSCPIA01_Oscar_Piastri/oscpia01.png.transform/2col-retina/image.png"
        lower.contains("sai") || lower.contains("sainz") -> "$base/C/CARSAI01_Carlos_Sainz/carsai01.png.transform/2col-retina/image.png"
        lower.contains("ham") || lower.contains("hamilton") -> "$base/L/LEWHAM01_Lewis_Hamilton/lewham01.png.transform/2col-retina/image.png"
        lower.contains("rus") || lower.contains("russell") -> "$base/G/GEORUS01_George_Russell/georus01.png.transform/2col-retina/image.png"
        lower.contains("alo") || lower.contains("alonso") -> "$base/F/FERALO01_Fernando_Alonso/feralo01.png.transform/2col-retina/image.png"
        lower.contains("gas") || lower.contains("gasly") -> "$base/P/PIEGAS01_Pierre_Gasly/piegas01.png.transform/2col-retina/image.png"
        lower.contains("hul") || lower.contains("hulkenberg") -> "$base/N/NICHUL01_Nico_Hulkenberg/nichul01.png.transform/2col-retina/image.png"
        lower.contains("tsu") || lower.contains("tsunoda") -> "$base/Y/YUKTSU01_Yuki_Tsunoda/yuktsu01.png.transform/2col-retina/image.png"
        lower.contains("law") || lower.contains("lawson") -> "$base/L/LIALAW01_Liam_Lawson/lialaw01.png.transform/2col-retina/image.png"
        lower.contains("bot") || lower.contains("bottas") -> "$base/V/VALBOT01_Valtteri_Bottas/valbot01.png.transform/2col-retina/image.png"
        lower.contains("bea") || lower.contains("bearman") -> "$base/O/OLIBEA01_Oliver_Bearman/olibea01.png.transform/2col-retina/image.png"
        lower.contains("col") || lower.contains("colapinto") -> "$base/F/FRACOL01_Franco_Colapinto/fracol01.png.transform/2col-retina/image.png"
        lower.contains("per") || lower.contains("perez") -> "$base/S/SERPER01_Sergio_Perez/serper01.png.transform/2col-retina/image.png"
        lower.contains("oco") || lower.contains("ocon") -> "$base/E/ESTOCO01_Esteban_Ocon/estoco01.png.transform/2col-retina/image.png"
        lower.contains("str") || lower.contains("stroll") -> "$base/L/LANSTR01_Lance_Stroll/lanstr01.png.transform/2col-retina/image.png"
        lower.contains("mag") || lower.contains("magnussen") -> "$base/K/KEVMAG01_Kevin_Magnussen/kevmag01.png.transform/2col-retina/image.png"
        lower.contains("zho") || lower.contains("zhou") -> "$base/G/GUAZHO01_Guanyu_Zhou/guazho01.png.transform/2col-retina/image.png"
        lower.contains("sar") || lower.contains("sargeant") -> "$base/L/LOGSAR01_Logan_Sargeant/logsar01.png.transform/2col-retina/image.png"
        lower.contains("ric") || lower.contains("ricciardo") -> "$base/D/DANRIC01_Daniel_Ricciardo/danric01.png.transform/2col-retina/image.png"
        lower.contains("doo") || lower.contains("doohan") -> "$base/J/JACDOO01_Jack_Doohan/jacdoo01.png.transform/2col-retina/image.png"
        else -> ""
    }
}

fun getCountryFlagUrl(countryOrCircuit: String): String {
    val lower = countryOrCircuit.lowercase().trim()
    val code = when {
        lower.contains("sepang") || lower.contains("malaysia") -> "my"
        lower.contains("australia") || lower.contains("melbourne") -> "au"
        lower.contains("china") || lower.contains("shanghai") -> "cn"
        lower.contains("japan") || lower.contains("suzuka") -> "jp"
        lower.contains("bahrain") || lower.contains("sakhir") -> "bh"
        lower.contains("saudi") || lower.contains("jeddah") -> "sa"
        lower.contains("usa") || lower.contains("united states") || lower.contains("miami") || lower.contains("austin") || lower.contains("vegas") -> "us"
        lower.contains("canada") || lower.contains("montreal") -> "ca"
        lower.contains("monaco") || lower.contains("monte carlo") -> "mc"
        lower.contains("spain") || lower.contains("catalunya") || lower.contains("barcelona") || lower.contains("madrid") -> "es"
        lower.contains("austria") || lower.contains("spielberg") -> "at"
        lower.contains("britain") || lower.contains("silverstone") || lower.contains("uk") -> "gb"
        lower.contains("belgium") || lower.contains("spa") -> "be"
        lower.contains("hungary") || lower.contains("hungaroring") -> "hu"
        lower.contains("netherlands") || lower.contains("dutch") || lower.contains("zandvoort") -> "nl"
        lower.contains("italy") || lower.contains("italian") || lower.contains("monza") || lower.contains("imola") -> "it"
        lower.contains("azerbaijan") || lower.contains("baku") -> "az"
        lower.contains("singapore") -> "sg"
        lower.contains("mexico") -> "mx"
        lower.contains("brazil") || lower.contains("interlagos") -> "br"
        lower.contains("qatar") || lower.contains("lusail") -> "qa"
        lower.contains("abu dhabi") || lower.contains("uae") -> "ae"
        lower.contains("france") || lower.contains("ricard") -> "fr"
        lower.contains("germany") || lower.contains("hockenheim") || lower.contains("nurburgring") -> "de"
        lower.contains("portugal") || lower.contains("portimao") || lower.contains("estoril") -> "pt"
        lower.contains("turkey") || lower.contains("istanbul") -> "tr"
        lower.contains("russia") || lower.contains("sochi") -> "ru"
        lower.contains("korea") -> "kr"
        lower.contains("india") || lower.contains("buddh") -> "in"
        lower.contains("south africa") || lower.contains("kyalami") -> "za"
        lower.contains("argentina") -> "ar"
        else -> "un"
    }
    return "https://flagcdn.com/w320/$code.png"
}

fun getDriverHeroCutoutUrl(codeOrId: String): String {
    val headshot = getDriverHeadshotUrl(codeOrId)
    return headshot.replace("2col-retina", "4col-retina")
}

fun getTeamLogoUrl(teamName: String): String {
    val lower = teamName.lowercase().trim()
    val base = "https://media.formula1.com/image/upload/c_lfill,w_256/q_auto/v1740000001/common/f1/2026"
    val file = when {
        lower.contains("mercedes") -> "mercedes/2026mercedeslogowhite.webp"
        lower.contains("ferrari") -> "ferrari/2026ferrarilogowhite.webp"
        lower.contains("mclaren") -> "mclaren/2026mclarenlogowhite.webp"
        lower.contains("red bull") || lower.contains("red_bull") -> "redbullracing/2026redbullracinglogowhite.webp"
        lower.contains("aston") -> "astonmartin/2026astonmartinlogowhite.webp"
        lower.contains("alpine") -> "alpine/2026alpinelogowhite.webp"
        lower.contains("williams") -> "williams/2026williamslogowhite.webp"
        lower.contains("haas") -> "haasf1team/2026haasf1teamlogowhite.webp"
        lower.contains("cadillac") -> "cadillac/2026cadillaclogowhite.webp"
        lower.contains("audi") -> "audi/2026audilogowhite.webp"
        lower.contains("sauber") || lower.contains("kick") -> "audi/2026audilogowhite.webp"
        lower.contains("rb") || lower.contains("racing bull") || lower.contains("racing_bulls") -> "racingbulls/2026racingbullslogowhite.webp"
        else -> "mercedes/2026mercedeslogowhite.webp"
    }
    return "$base/$file"
}

fun getTeamCarUrl(teamName: String): String {
    val lower = teamName.lowercase().trim()
    if (lower.contains("cadillac")) {
        return "https://media.formula1.com/image/upload/c_lfill,h_224/q_auto/d_common:f1:2026:fallback:car:2026fallbackcarright.webp/v1740000001/common/f1/2026/cadillac/2026cadillaccarright.webp"
    }
    val slug = when {
        lower.contains("mercedes") -> "mercedes"
        lower.contains("ferrari") -> "ferrari"
        lower.contains("mclaren") -> "mclaren"
        lower.contains("red bull") || lower.contains("red_bull") -> "red-bull-racing"
        lower.contains("aston") -> "aston-martin"
        lower.contains("alpine") -> "alpine"
        lower.contains("williams") -> "williams"
        lower.contains("rb") || lower.contains("racing bull") -> "rb"
        lower.contains("haas") -> "haas-f1-team"
        lower.contains("audi") || lower.contains("sauber") || lower.contains("kick") -> "kick-sauber"
        else -> "mercedes"
    }
    return "$F1_CDN/d_team_car_fallback_image.png/content/dam/fom-website/teams/2024/$slug.png.transform/6col-retina/image.png"
}

fun getCircuitSlug(circuitId: String?): String {
    if (circuitId.isNullOrBlank()) return "monza-7"
    val lower = circuitId.lowercase().trim()
    if (CircuitPathRepository.getPreloadedPaths(lower) != null) return lower
    return when {
        lower.contains("madrid") || lower.contains("madring") -> "madring-1"
        lower.contains("catalunya") || lower.contains("barcelona") || lower.contains("spain") -> "catalunya-6"
        lower.contains("monza") || lower.contains("italy") || lower.contains("italian") -> "monza-7"
        lower.contains("silverstone") || lower.contains("britain") || lower.contains("british") || lower.contains("uk") -> "silverstone-8"
        lower.contains("spa") || lower.contains("belgium") || lower.contains("francorchamps") -> "spa-francorchamps-4"
        lower.contains("albert") || lower.contains("melbourne") || lower.contains("australia") -> "melbourne-2"
        lower.contains("monaco") || lower.contains("monte") -> "monaco-6"
        lower.contains("hungaroring") || lower.contains("hungary") -> "hungaroring-3"
        lower.contains("interlagos") || lower.contains("brazil") || lower.contains("sao_paulo") || lower.contains("jose carlos") -> "interlagos-2"
        lower.contains("red_bull") || lower.contains("spielberg") || lower.contains("austria") -> "spielberg-3"
        lower.contains("villeneuve") || lower.contains("canada") || lower.contains("montreal") -> "montreal-6"
        lower.contains("suzuka") || lower.contains("japan") -> "suzuka-2"
        lower.contains("yas") || lower.contains("abu_dhabi") || lower.contains("uae") -> "yas-marina-2"
        lower.contains("zandvoort") || lower.contains("dutch") || lower.contains("netherlands") -> "zandvoort-5"
        lower.contains("marina") || lower.contains("singapore") -> "marina-bay-4"
        lower.contains("rodriguez") || lower.contains("mexico") || lower.contains("hermanos") -> "mexico-city-3"
        lower.contains("bahrain") || lower.contains("sakhir") -> "bahrain-1"
        lower.contains("baku") || lower.contains("azerbaijan") -> "baku-1"
        lower.contains("jeddah") || lower.contains("saudi") -> "jeddah-1"
        lower.contains("losail") || lower.contains("lusail") || lower.contains("qatar") -> "lusail-1"
        lower.contains("vegas") || lower.contains("las_vegas") -> "las-vegas-1"
        lower.contains("miami") -> "miami-1"
        lower.contains("shanghai") || lower.contains("china") -> "shanghai-1"
        lower.contains("americas") || lower.contains("austin") || lower.contains("cota") -> "austin-1"
        lower.contains("imola") || lower.contains("emilia") -> "monza-7" // Fallback to Italian sister track if not standalone
        else -> "monza-7"
    }
}

fun getCircuitMapUrl(circuitId: String?): String {
    val slug = getCircuitSlug(circuitId)
    return "https://raw.githubusercontent.com/f1db/f1-circuits-svg/main/circuits/detailed/white-outline/$slug.svg"
}

fun format2Digits(n: Int): String = if (n < 10) "0$n" else "$n"

fun getCurrentDateIso(): String {
    val date = GMTDate()
    val m = date.month.ordinal + 1
    val d = date.dayOfMonth
    return "${date.year}-${format2Digits(m)}-${format2Digits(d)}"
}

data class CountdownRemaining(
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val isPastOrLive: Boolean
)

fun parseUtcDateTimeToEpochMillis(dateStr: String?, timeStr: String?): Long? {
    if (dateStr.isNullOrBlank()) return null
    val dateParts = dateStr.trim().split("-")
    if (dateParts.size != 3) return null
    val year = dateParts[0].toIntOrNull() ?: return null
    val month = dateParts[1].toIntOrNull() ?: return null
    val day = dateParts[2].toIntOrNull() ?: return null

    var hour = 13 // standard default 13:00 UTC if time not specified
    var minute = 0
    var second = 0

    if (!timeStr.isNullOrBlank()) {
        val cleanTime = timeStr.trim().removeSuffix("Z").removeSuffix("z")
        val timeParts = cleanTime.split(":")
        if (timeParts.isNotEmpty()) hour = timeParts[0].toIntOrNull() ?: 13
        if (timeParts.size > 1) minute = timeParts[1].toIntOrNull() ?: 0
        if (timeParts.size > 2) second = timeParts[2].toIntOrNull() ?: 0
    }

    var y = year
    var m = month
    if (m <= 2) {
        y -= 1
        m += 12
    }
    val era = (if (y >= 0) y else y - 399) / 400
    val yoe = y - era * 400
    val doy = (153 * (m - 3) + 2) / 5 + day - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    val daysSinceEpoch = era * 146097L + doe - 719468L

    val totalSeconds = daysSinceEpoch * 86400L + hour * 3600L + minute * 60L + second
    return totalSeconds * 1000L
}

fun calculateCountdownRemaining(targetEpochMillis: Long, currentEpochMillis: Long = GMTDate().timestamp): CountdownRemaining {
    val diffMillis = targetEpochMillis - currentEpochMillis
    if (diffMillis <= 0) {
        return CountdownRemaining(0, 0, 0, 0, isPastOrLive = true)
    }
    val totalSeconds = diffMillis / 1000L
    val days = (totalSeconds / 86400L).toInt()
    val hours = ((totalSeconds % 86400L) / 3600L).toInt()
    val minutes = ((totalSeconds % 3600L) / 60L).toInt()
    val seconds = (totalSeconds % 60L).toInt()
    return CountdownRemaining(days, hours, minutes, seconds, isPastOrLive = false)
}

data class CircuitSpecs(
    val direction: String,
    val turns: Int,
    val drsZones: Int,
    val lengthKm: String,
    val laps: Int,
    val raceDistanceKm: String,
    val firstGrandPrix: Int,
    val lapRecordHolder: String,
    val lapRecordTime: String
)

fun getCircuitSpecs(circuitId: String?): CircuitSpecs {
    val id = circuitId?.lowercase()?.trim() ?: ""
    return when {
        id.contains("monza") || id.contains("italy") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 11,
            drsZones = 2,
            lengthKm = "5.793 KM",
            laps = 53,
            raceDistanceKm = "306.72 KM",
            firstGrandPrix = 1950,
            lapRecordHolder = "R. Barrichello (2004)",
            lapRecordTime = "1:21.046"
        )
        id.contains("silverstone") || id.contains("british") || id.contains("uk") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 18,
            drsZones = 2,
            lengthKm = "5.891 KM",
            laps = 52,
            raceDistanceKm = "306.19 KM",
            firstGrandPrix = 1950,
            lapRecordHolder = "M. Verstappen (2020)",
            lapRecordTime = "1:27.097"
        )
        id.contains("spa") || id.contains("belgian") || id.contains("francorchamps") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 19,
            drsZones = 2,
            lengthKm = "7.004 KM",
            laps = 44,
            raceDistanceKm = "308.05 KM",
            firstGrandPrix = 1950,
            lapRecordHolder = "V. Bottas (2018)",
            lapRecordTime = "1:46.286"
        )
        id.contains("monaco") || id.contains("monte") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 19,
            drsZones = 1,
            lengthKm = "3.337 KM",
            laps = 78,
            raceDistanceKm = "260.28 KM",
            firstGrandPrix = 1950,
            lapRecordHolder = "L. Hamilton (2021)",
            lapRecordTime = "1:12.909"
        )
        id.contains("suzuka") || id.contains("japan") -> CircuitSpecs(
            direction = "Figure-8",
            turns = 18,
            drsZones = 1,
            lengthKm = "5.807 KM",
            laps = 53,
            raceDistanceKm = "307.47 KM",
            firstGrandPrix = 1987,
            lapRecordHolder = "L. Hamilton (2019)",
            lapRecordTime = "1:30.983"
        )
        id.contains("interlagos") || id.contains("brazil") || id.contains("sao_paulo") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 15,
            drsZones = 2,
            lengthKm = "4.309 KM",
            laps = 71,
            raceDistanceKm = "305.87 KM",
            firstGrandPrix = 1973,
            lapRecordHolder = "V. Bottas (2018)",
            lapRecordTime = "1:10.540"
        )
        id.contains("catalunya") || id.contains("barcelona") || id.contains("spanish") || id.contains("spain") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 14,
            drsZones = 2,
            lengthKm = "4.657 KM",
            laps = 66,
            raceDistanceKm = "307.23 KM",
            firstGrandPrix = 1991,
            lapRecordHolder = "M. Verstappen (2023)",
            lapRecordTime = "1:16.330"
        )
        id.contains("albert") || id.contains("melbourne") || id.contains("australian") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 14,
            drsZones = 4,
            lengthKm = "5.278 KM",
            laps = 58,
            raceDistanceKm = "306.12 KM",
            firstGrandPrix = 1996,
            lapRecordHolder = "C. Leclerc (2024)",
            lapRecordTime = "1:19.813"
        )
        id.contains("red_bull") || id.contains("spielberg") || id.contains("austrian") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 10,
            drsZones = 3,
            lengthKm = "4.318 KM",
            laps = 71,
            raceDistanceKm = "306.45 KM",
            firstGrandPrix = 1970,
            lapRecordHolder = "C. Sainz (2020)",
            lapRecordTime = "1:05.619"
        )
        id.contains("marina") || id.contains("singapore") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 19,
            drsZones = 3,
            lengthKm = "4.940 KM",
            laps = 62,
            raceDistanceKm = "306.14 KM",
            firstGrandPrix = 2008,
            lapRecordHolder = "D. Ricciardo (2024)",
            lapRecordTime = "1:34.486"
        )
        id.contains("villeneuve") || id.contains("montreal") || id.contains("canadian") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 14,
            drsZones = 2,
            lengthKm = "4.361 KM",
            laps = 70,
            raceDistanceKm = "305.27 KM",
            firstGrandPrix = 1978,
            lapRecordHolder = "V. Bottas (2019)",
            lapRecordTime = "1:13.078"
        )
        id.contains("bahrain") || id.contains("sakhir") -> CircuitSpecs(
            direction = "Clockwise",
            turns = 15,
            drsZones = 3,
            lengthKm = "5.412 KM",
            laps = 57,
            raceDistanceKm = "308.23 KM",
            firstGrandPrix = 2004,
            lapRecordHolder = "P. de la Rosa (2005)",
            lapRecordTime = "1:31.447"
        )
        id.contains("jeddah") || id.contains("saudi") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 27,
            drsZones = 3,
            lengthKm = "6.174 KM",
            laps = 50,
            raceDistanceKm = "308.45 KM",
            firstGrandPrix = 2021,
            lapRecordHolder = "L. Hamilton (2021)",
            lapRecordTime = "1:30.734"
        )
        id.contains("vegas") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 17,
            drsZones = 2,
            lengthKm = "6.201 KM",
            laps = 50,
            raceDistanceKm = "309.95 KM",
            firstGrandPrix = 2023,
            lapRecordHolder = "O. Piastri (2023)",
            lapRecordTime = "1:35.490"
        )
        id.contains("miami") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 19,
            drsZones = 3,
            lengthKm = "5.412 KM",
            laps = 57,
            raceDistanceKm = "308.32 KM",
            firstGrandPrix = 2022,
            lapRecordHolder = "M. Verstappen (2023)",
            lapRecordTime = "1:29.708"
        )
        id.contains("americas") || id.contains("austin") || id.contains("cota") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 20,
            drsZones = 2,
            lengthKm = "5.513 KM",
            laps = 56,
            raceDistanceKm = "308.40 KM",
            firstGrandPrix = 2012,
            lapRecordHolder = "C. Leclerc (2019)",
            lapRecordTime = "1:36.169"
        )
        id.contains("yas") || id.contains("abu_dhabi") -> CircuitSpecs(
            direction = "Anti-Clockwise",
            turns = 16,
            drsZones = 2,
            lengthKm = "5.281 KM",
            laps = 58,
            raceDistanceKm = "306.18 KM",
            firstGrandPrix = 2009,
            lapRecordHolder = "M. Verstappen (2021)",
            lapRecordTime = "1:26.103"
        )
        else -> CircuitSpecs(
            direction = "Clockwise",
            turns = 16,
            drsZones = 2,
            lengthKm = "5.380 KM",
            laps = 55,
            raceDistanceKm = "306.00 KM",
            firstGrandPrix = 2000,
            lapRecordHolder = "L. Norris (2024)",
            lapRecordTime = "1:24.450"
        )
    }
}

