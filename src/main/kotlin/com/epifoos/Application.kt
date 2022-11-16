package com.epifoos

import com.epifoos.config.Config
import com.epifoos.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*

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

class InsensitiveLikeOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "ILIKE")
infix fun<T:String?> ExpressionWithColumnType<T>.ilike(pattern: String): Op<Boolean> = InsensitiveLikeOp(this, QueryParameter(pattern, columnType))
