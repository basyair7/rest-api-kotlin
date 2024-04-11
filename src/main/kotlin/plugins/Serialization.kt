package com.basyair7.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            // optional configurate
            // ini tempat untuk menambahkan modul json/jackson
        }
    }
}