package com.dirzaaulia.formula1.model.response

import com.dirzaaulia.formula1.model.Driver
import kotlinx.serialization.Serializable

@Serializable
data class DriverResponse(
    val driver: List<Driver>
)