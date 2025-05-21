package com.dirzaaulia.formula1.model

import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    val driverId: String? = null,
    val name: String,
    val surname: String,
    val nationality: String,
    val birthday: String,
    val number: Int,
    val shortName: String,
    val url: String
)