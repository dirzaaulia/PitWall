package com.dirzaaulia.formula1.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class RaceResult(
    val fp1Id: Int = -1,
    val fp2Id: Int = -1,
    val fp3Id: Int = -1,
    @Serializable(with = IntOrStringSerializer::class)
    val position: Any = 0,
    @Serializable(with = IntOrStringSerializer::class)
    val qualifyingPosition: Any = -1,
    val classificationId: Int = -1,
    val q1: String? = "",
    val q2: String? = "",
    val q3: String? = "",
    val sq1: String? = "",
    val sq2: String? = "",
    val sq3: String? = "",
    val points: Int = -1,
    @Serializable(with = IntOrStringSerializer::class)
    val grid: Any = 0,
    @Serializable(with = IntOrStringSerializer::class)
    val gridPosition: Any = 0,
    val time: String = "",
    val fastLap: String? = "",
    val driver: Driver,
    val team: Team
)

@Serializer(forClass = Any::class)
object IntOrStringSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IntOrString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is Int -> encoder.encodeInt(value)
            is String -> encoder.encodeString(value)
            else -> throw SerializationException("Unsupported type")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        val input = decoder.decodeString()
        return input.toIntOrNull() ?: input
    }
}