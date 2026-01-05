package com.github.aeoliux.violet.api.bodys

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull

@Serializable
data class IdAndUrl(val Id: Int, val Url: String)

@Serializable
data class IdAsStringAndUrl(val Id: String, val Url: String)

object HybridIdDeserializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("HybridId", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Int {
        val decoder = decoder as? JsonDecoder ?: error("HybridId works only with JSON")
        val element = decoder.decodeJsonElement()

        return when (element) {
            is JsonPrimitive -> when {
                element.isString -> element.content.trimStart('t').toInt()
                element.intOrNull != null -> element.int
                else -> error("Invalid ID type")
            }
            else -> error("Invalid ID Type")
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: Int
    ) {
        error("Not serializable")
    }
}