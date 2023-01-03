package com.epifoos.plugins

import io.ktor.server.application.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureMonitoring() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

//    install(MicrometerMetrics) {
//        registry = appMicrometerRegistry
//        // ...
//    }
//
//    install(CallLogging) {
//        level = Level.INFO
//        filter { call -> call.request.path().startsWith("/") }
//    }
//
//    routing {
//        get("/metrics-micrometer") {
//            call.respond(appMicrometerRegistry.scrape())
//        }
//    }
}
