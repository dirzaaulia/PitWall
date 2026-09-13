package com.dirzaaulia.formula1.util

import com.dirzaaulia.formula1.model.Race
import io.ktor.util.date.GMTDate

data class UpcomingSession(
    val sessionName: String,       // e.g. "FREE PRACTICE 1", "QUALIFYING", "SPRINT RACE", "GRAND PRIX RACE"
    val sessionBadge: String,      // e.g. "FP1", "FP2", "QUALI", "SPRINT", "RACE"
    val targetEpochMillis: Long,   // Exact UTC timestamp for countdown
    val isRaceSession: Boolean
)

object SessionCountdownScheduler {

    /**
     * Resolves the next upcoming live weekend session (Practice, Qualifying, Sprint, or Race)
     * based on the race's official FIA weekend schedule and current UTC time.
     */
    fun resolveNextUpcomingSession(
        race: Race,
        nowEpochMillis: Long = GMTDate().timestamp
    ): UpcomingSession {
        val schedule = race.schedule
        val candidates = mutableListOf<UpcomingSession>()

        fun addCandidate(date: String?, time: String?, name: String, badge: String, isRace: Boolean = false) {
            if (!date.isNullOrBlank()) {
                val epoch = parseUtcDateTimeToEpochMillis(date, time.orEmpty())
                if (epoch != null) {
                    candidates.add(UpcomingSession(name, badge, epoch, isRace))
                }
            }
        }

        if (schedule != null) {
            // Free Practice 1
            addCandidate(schedule.fp1?.date, schedule.fp1?.time, "FREE PRACTICE 1", "FP1")

            if (race.isSprint) {
                // Sprint format
                addCandidate(schedule.sprintQualy?.date, schedule.sprintQualy?.time, "SPRINT QUALIFYING", "SQ")
                addCandidate(schedule.sprintRace?.date, schedule.sprintRace?.time, "SPRINT RACE", "SPRINT")
                addCandidate(schedule.qualy?.date, schedule.qualy?.time, "QUALIFYING", "QUALI")
            } else {
                // Standard format
                addCandidate(schedule.fp2?.date, schedule.fp2?.time, "FREE PRACTICE 2", "FP2")
                addCandidate(schedule.fp3?.date, schedule.fp3?.time, "FREE PRACTICE 3", "FP3")
                addCandidate(schedule.qualy?.date, schedule.qualy?.time, "QUALIFYING", "QUALI")
            }

            // Sunday Grand Prix Race
            val rDate = schedule.race?.date ?: race.date
            val rTime = schedule.race?.time ?: race.time
            addCandidate(rDate, rTime, "GRAND PRIX RACE", "RACE", isRace = true)
        } else {
            val raceEpoch = parseUtcDateTimeToEpochMillis(race.date, race.time)
                ?: (nowEpochMillis + 7L * 86400L * 1000L)
            candidates.add(UpcomingSession("GRAND PRIX RACE", "RACE", raceEpoch, isRaceSession = true))
        }

        candidates.sortBy { it.targetEpochMillis }

        // Pick earliest upcoming session whose start time is in the future.
        // Once a session has started or concluded (e.g. FP3 is over), advance to the next upcoming session (Qualifying).
        val next = candidates.firstOrNull { it.targetEpochMillis > nowEpochMillis }

        return next ?: candidates.lastOrNull { it.isRaceSession } ?: candidates.firstOrNull() ?: UpcomingSession(
            sessionName = "GRAND PRIX RACE",
            sessionBadge = "RACE",
            targetEpochMillis = nowEpochMillis + 14L * 86400L * 1000L,
            isRaceSession = true
        )
    }
}
