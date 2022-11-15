package com.epifoos.plugins

import com.epifoos.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
    val securityConfig = Config.getSecurityConfig()

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Head)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        if (securityConfig.cors == null) anyHost() else securityConfig.cors.forEach { allowHost(it, listOf("http", "https")) }
    }
    install(Compression) {
        gzip()
        deflate()
    }

}

