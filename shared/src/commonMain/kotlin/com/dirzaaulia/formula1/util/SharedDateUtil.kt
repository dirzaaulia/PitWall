package com.dirzaaulia.formula1.util

fun formatUtcToLocalTime(dateStr: String, timeStr: String): String {
    if (dateStr.isBlank()) return "TBD"
    val cleanTime = if (timeStr.isNotBlank()) timeStr.replace("Z", "").trim() else "12:00:00"
    val parts = dateStr.split("-")
    if (parts.size < 3) return dateStr
    val month = parts[1].toIntOrNull() ?: 1
    val day = parts[2].toIntOrNull() ?: 1

    val monthName = when (month) {
        1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"; 5 -> "May"; 6 -> "Jun"
        7 -> "Jul"; 8 -> "Aug"; 9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
        else -> ""
    }

    val timeShort = if (cleanTime.contains(":")) {
        val tParts = cleanTime.split(":")
        "${tParts[0]}:${tParts[1]}"
    } else cleanTime

    return "$day $monthName • $timeShort"
}
