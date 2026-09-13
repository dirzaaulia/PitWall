package com.dirzaaulia.formula1.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Driver(
    val driverId: String = "",
    val name: String = "",
    val surname: String = "",
    val nationality: String = "",
    val birthday: String = "",
    val number: Int = 0,
    val shortName: String = "",
    val code: String = "",
    val team: String = "",
    @Transient val teamColor: Color = Color(0xFFE10600),
    val points: Double = 0.0,
    val wins: Int = 0,
    val url: String = ""
) {
    val fullName: String
        get() = if (name.isNotBlank() && surname.isNotBlank()) "$name $surname" else name.ifBlank { surname }
}
