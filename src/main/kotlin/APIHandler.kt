package com.basyair7

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class APIHandler(val portWeb: Int) {
    fun put(Param: String) {
        embeddedServer(Netty, portWeb) {
            routing {
                put("/item/{$Param}") {
                    val itemId = call.parameters["Param"]
                    call.respondText("Item $itemId sudah diperbarui")
                }
            }
        }.start(true)
    }
    
    fun testAPI() {
        embeddedServer(Netty, portWeb) {
            routing {
                get("/") {
                    call.respondText("Hello, Rest API in Kotlin!")
                }
            }
        }.start(true)
    }

    fun HelloWorld(Name: String) {
        println("Hello World, $Name")
    }
}