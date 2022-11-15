package com.epifoos

import com.epifoos.config.Config
import com.epifoos.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = Config.getPort(), module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabase()
    configureHTTP()
    configureAdministration()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureMonitoring()
}


