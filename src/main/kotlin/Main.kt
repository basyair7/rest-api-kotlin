package com.basyair7

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val dbHandler = DatabaseHandler()
    val apiHandler = APIHandler(dbHandler)

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                // optional configurate
                // ini tempat untuk menambahkan modul json/jackson
            }
        }

        routing {
            post("/sensor") {
                apiHandler.saveData(call)
            }

            put("/sensor/{id}") {
                apiHandler.updateData(call)
            }

            get("/sensor") {
                apiHandler.getAllData(call)
            }

            get("/test") {
                apiHandler.testAPI(call)
            }

            get("/sensor/{id}") {
                apiHandler.getDataById(call)
            }

            delete("/sensor/{id}") {
                apiHandler.deleteDataById(call)
            }
        }
    }.start(wait = true)
}
