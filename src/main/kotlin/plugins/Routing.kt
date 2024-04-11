package com.basyair7.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*

fun Application.configureRouting() {
    val dbHandler = DatabaseHandler()
    val apiHandler = APIHandler(dbHandler)

    routing {
        authenticate("Security-myAPI") {
            get("/test") {
                apiHandler.testGetAPI(call)
            }

            post("/test") {
                apiHandler.testPostAPI(call)
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
}