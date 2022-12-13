package com.epifoos.config.serializers

import com.epifoos.domain.Elo
import com.fasterxml.jackson.databind.module.SimpleModule

class CustomModule: SimpleModule {

    constructor() {
        addSerializer(Elo::class.java, EloSerializer())
    }

}
