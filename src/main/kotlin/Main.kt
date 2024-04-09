package com.basyair7

import io.github.cdimascio.dotenv.Dotenv
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val dbHandler = DatabaseHandler()
    val apiHandler = APIHandler(dbHandler)
    val dotenv = Dotenv.configure().load()

    embeddedServer(Netty, port = 8080) {
        install(Authentication) {
            basic("basic-auth") {
                realm = "Ktor Server"
                validate { credentials ->
                    if(credentials.name == dotenv["USER"] && credentials.password == dotenv["PASSWORD"])
                    {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
            }
        }
        install(ContentNegotiation) {
            jackson {
                // optional configurate
                // ini tempat untuk menambahkan modul json/jackson
            }
        }

        routing {
            authenticate("basic-auth") {
                get("/test") {
                    apiHandler.testgetAPI(call)
                }

                post("/test") {
                    apiHandler.testpostAPI(call)
                }

                post("/sensor") {
                    apiHandler.saveData(call)
                }

                put("/sensor/{id}") {
                    apiHandler.updateData(call)
                }

                get("/sensor") {
                    apiHandler.getAllData(call)
                }

                get("/sensor/{id}") {
                    apiHandler.getDataById(call)
                }

                delete("/sensor/{id}") {
                    apiHandler.deleteDataById(call)
                }
            }
        }
    }.start(wait = true)
}
