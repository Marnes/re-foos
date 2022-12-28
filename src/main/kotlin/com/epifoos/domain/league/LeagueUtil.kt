package com.epifoos.domain.league

import java.util.*

object LeagueUtil {

    fun generateUid(): String {
        return UUID.randomUUID().toString().substring(0, 7)
    }
}
