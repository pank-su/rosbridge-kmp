package com.github.thoebert.krosbridge.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder

object IntOrStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor
        get() = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder ?: throw Exception("Only JSON format is expected")
        try {
            return jsonDecoder.decodeString()

        }catch (e: Exception){
            return jsonDecoder.decodeInt().toString()
        }

    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }



}