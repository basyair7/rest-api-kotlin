package com.basyair7.plugins

import io.github.cdimascio.dotenv.Dotenv
import io.ktor.application.*
import io.ktor.auth.*

fun Application.configureSecurity() {
    val dotenv = Dotenv.configure().load()
    authentication {
        basic(name="Security-myAPI") {
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
}