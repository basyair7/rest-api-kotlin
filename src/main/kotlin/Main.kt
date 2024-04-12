package com.basyair7

import com.basyair7.plugins.configureRouting
import com.basyair7.plugins.configureSecurity
import com.basyair7.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 80, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRouting()
}
