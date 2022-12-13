package com.epifoos.config.serializers

import com.epifoos.domain.Elo
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class EloSerializer: JsonSerializer<Elo>() {

    override fun serialize(value: Elo?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        value?.let { gen?.writeNumber(it.toDouble()) } ?: gen?.writeNull()
    }
}
