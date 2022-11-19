package com.epifoos

import com.epifoos.plugins.configureRouting
import io.ktor.server.testing.*
import kotlin.test.Ignore
import kotlin.test.Test

class ApplicationTest {
    @Test
    @Ignore
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
    }
}
