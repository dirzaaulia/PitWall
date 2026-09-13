package com.dirzaaulia.formula1.util

import kotlin.math.abs
import kotlin.math.roundToInt

data class LiveCarTelemetry(
    val speedKmh: Int,
    val rpm: Int,
    val gear: Int,
    val throttlePercent: Int,
    val brakePercent: Int,
    val isDrsAvailable: Boolean,
    val isDrsActive: Boolean
)

data class LiveTyreTelemetry(
    val flWearPercent: Int,
    val frWearPercent: Int,
    val rlWearPercent: Int,
    val rrWearPercent: Int,
    val flTempC: Int,
    val frTempC: Int,
    val rlTempC: Int,
    val rrTempC: Int,
    val overallHealth: String
)

object TelemetryPhysicsEngine {

    private data class TrackTelemetryNode(
        val fraction: Float,
        val speedKmh: Float,
        val throttle: Float,
        val brake: Float,
        val lateralG: Float
    )

    // Monza Grand Prix Continuous Lap Profile (Seamless wrap at 1.0f -> 0.0f)
    private val CIRCUIT_NODES = listOf(
        TrackTelemetryNode(0.00f, 330f, 100f, 0f, 0.1f),   // Start / Finish Line
        TrackTelemetryNode(0.08f, 348f, 100f, 0f, 0.2f),   // Rettifilo Straight Braking Point
        TrackTelemetryNode(0.12f, 72f, 0f, 96f, 1.2f),     // Turn 1 (Prima Variante chicane apex)
        TrackTelemetryNode(0.15f, 98f, 65f, 0f, 1.8f),     // Turn 2 exit
        TrackTelemetryNode(0.24f, 305f, 100f, 0f, 2.9f),   // Curva Grande (Turn 3)
        TrackTelemetryNode(0.30f, 112f, 0f, 88f, 1.4f),    // Variante della Roggia (Turn 4-5)
        TrackTelemetryNode(0.35f, 145f, 75f, 0f, 2.1f),    // Roggia exit
        TrackTelemetryNode(0.42f, 215f, 85f, 0f, 2.7f),    // Curva di Lesmo 1 (Turn 6)
        TrackTelemetryNode(0.48f, 168f, 55f, 45f, 3.1f),   // Curva di Lesmo 2 (Turn 7 apex)
        TrackTelemetryNode(0.60f, 340f, 100f, 0f, 0.2f),   // Serraglio DRS Straight
        TrackTelemetryNode(0.66f, 138f, 0f, 85f, 2.8f),    // Variante Ascari entry (Turn 8)
        TrackTelemetryNode(0.72f, 230f, 85f, 0f, 3.0f),    // Ascari exit (Turn 10)
        TrackTelemetryNode(0.82f, 332f, 100f, 0f, 0.1f),   // Rettifilo della Roggia / Back Straight
        TrackTelemetryNode(0.88f, 172f, 0f, 68f, 3.4f),    // Curva Parabolica (Alboreto Turn 11 entry)
        TrackTelemetryNode(0.94f, 245f, 85f, 0f, 3.2f),    // Parabolica apex / mid-corner acceleration
        TrackTelemetryNode(1.00f, 330f, 100f, 0f, 0.1f)    // Seamless wrap-around to 0.00f!
    )

    private fun smoothStep(t: Float): Float = t * t * (3f - 2f * t)

    /**
     * Calculates smooth, continuous car telemetry for any point along a lap.
     * Guaranteed 0 discontinuous jumps even across lap boundary changes.
     */
    fun calculateLiveTelemetry(subLapFraction: Float): LiveCarTelemetry {
        val f = subLapFraction.coerceIn(0f, 0.9999f)

        // Find surrounding nodes
        var prev = CIRCUIT_NODES[0]
        var next = CIRCUIT_NODES[1]
        for (i in 0 until CIRCUIT_NODES.size - 1) {
            if (f >= CIRCUIT_NODES[i].fraction && f <= CIRCUIT_NODES[i + 1].fraction) {
                prev = CIRCUIT_NODES[i]
                next = CIRCUIT_NODES[i + 1]
                break
            }
        }

        val span = (next.fraction - prev.fraction).coerceAtLeast(0.0001f)
        val rawT = (f - prev.fraction) / span
        val t = smoothStep(rawT.coerceIn(0f, 1f))

        val speed = prev.speedKmh + (next.speedKmh - prev.speedKmh) * t
        val throttle = prev.throttle + (next.throttle - prev.throttle) * t
        val brake = prev.brake + (next.brake - prev.brake) * t

        val speedInt = speed.roundToInt().coerceIn(60, 360)
        val throttleInt = throttle.roundToInt().coerceIn(0, 100)
        val brakeInt = brake.roundToInt().coerceIn(0, 100)

        // Realistic gear determination
        val gear = when {
            speedInt > 292 -> 8
            speedInt > 248 -> 7
            speedInt > 200 -> 6
            speedInt > 158 -> 5
            speedInt > 120 -> 4
            speedInt > 85 -> 3
            else -> 2
        }

        // Realistic RPM modeling per gear
        val baseGearRpm = when (gear) {
            8 -> 10800 + ((speedInt - 292) / 60f * 1700f).roundToInt()
            7 -> 10600 + ((speedInt - 248) / 44f * 1800f).roundToInt()
            6 -> 10500 + ((speedInt - 200) / 48f * 1900f).roundToInt()
            5 -> 10400 + ((speedInt - 158) / 42f * 2000f).roundToInt()
            4 -> 10200 + ((speedInt - 120) / 38f * 2100f).roundToInt()
            3 -> 10000 + ((speedInt - 85) / 35f * 2200f).roundToInt()
            else -> 9800 + ((speedInt - 60) / 25f * 2400f).roundToInt()
        }
        val rpm = baseGearRpm.coerceIn(9400, 12650)

        // DRS detection on designated detection & activation zones
        val isDrsAvailable = f in 0.00f..0.07f || f in 0.52f..0.62f || f in 0.94f..1.00f
        val isDrsActive = isDrsAvailable && throttleInt > 85 && speedInt > 250

        return LiveCarTelemetry(
            speedKmh = speedInt,
            rpm = rpm,
            gear = gear,
            throttlePercent = throttleInt,
            brakePercent = brakeInt,
            isDrsAvailable = isDrsAvailable,
            isDrsActive = isDrsActive
        )
    }

    /**
     * Calculates dynamic tyre wear and temperatures based on tyre compound, age, and track dynamics.
     */
    fun calculateLiveTyreTelemetry(
        compound: String,
        tyreAgeLaps: Int,
        currentLap: Int,
        subLapFraction: Float
    ): LiveTyreTelemetry {
        val compoundUpper = compound.uppercase().trim()
        val wearRatePerLap = when (compoundUpper) {
            "SOFT" -> 2.6f
            "HARD" -> 1.1f
            "INTERMEDIATE" -> 2.0f
            "WET" -> 2.2f
            else -> 1.7f // Medium default
        }

        val baseWear = (100f - (tyreAgeLaps * wearRatePerLap)).coerceIn(25f, 100f)

        // Asymmetric wear (Front-Left takes highest load at Monza/clockwise circuits)
        val flWear = (baseWear - 1.2f).roundToInt().coerceIn(15, 100)
        val frWear = (baseWear + 0.8f).roundToInt().coerceIn(15, 100)
        val rlWear = (baseWear - 0.4f).roundToInt().coerceIn(15, 100)
        val rrWear = (baseWear + 0.5f).roundToInt().coerceIn(15, 100)

        // Dynamic Temperature based on braking and cornering along the lap
        val tele = calculateLiveTelemetry(subLapFraction)
        val brakeHeat = (tele.brakePercent * 0.16f).roundToInt()
        val isCornering = tele.speedKmh in 120..240 && tele.throttlePercent > 40
        val cornerHeat = if (isCornering) 8 else 0

        val flTemp = (98 + brakeHeat + cornerHeat + 3).coerceIn(85, 125)
        val frTemp = (97 + brakeHeat + cornerHeat).coerceIn(85, 125)
        val rlTemp = (95 + (brakeHeat * 0.6f).roundToInt() + cornerHeat).coerceIn(85, 120)
        val rrTemp = (96 + (brakeHeat * 0.6f).roundToInt() + cornerHeat + 1).coerceIn(85, 120)

        val health = when {
            flWear > 70 -> "OPTIMAL"
            flWear > 45 -> "STABLE"
            flWear > 25 -> "CLIFF APPROACHING"
            else -> "BOX THIS LAP"
        }

        return LiveTyreTelemetry(
            flWearPercent = flWear,
            frWearPercent = frWear,
            rlWearPercent = rlWear,
            rrWearPercent = rrWear,
            flTempC = flTemp,
            frTempC = frTemp,
            rlTempC = rlTemp,
            rrTempC = rrTemp,
            overallHealth = health
        )
    }
}
