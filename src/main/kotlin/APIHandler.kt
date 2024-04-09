package com.basyair7

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.serialization.Serializable

@Serializable
data class SensorDataAPI(val temperature: Double, val humidity: Double)

class APIHandler(private val dbHandler: DatabaseHandler) {
    suspend fun testAPI(call: ApplicationCall) {
        call.respondText("{\"test\": \"hello\"}")
    }

    suspend fun saveData(call: ApplicationCall) {
        try {
            val param = call.receive<SensorDataAPI>()
            val result = dbHandler.saveData(
                temperature = param.temperature,
                humidity = param.humidity
            )
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respondText("Data sensor tidak berhasil disimpan", status = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun updateData(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val sensorData = call.receive<SensorDataAPI>()
                val result = dbHandler.updateData(id, sensorData.temperature, sensorData.humidity)
                if (result != null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respondText("Data sensor dengan ID $id tidak ditemukan", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "ID tidak valid")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun getAllData(call: ApplicationCall) {
        try {
            val sensorDataList = dbHandler.findAllData()
            if (!sensorDataList.isEmpty()) {
                call.respond(sensorDataList)
            } else {
                call.respondText("Tidak ada data", status = HttpStatusCode.NotFound)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun getDataById(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val sensorData = dbHandler.findDataById(id)
                if (sensorData != null) {
                    call.respond(sensorData)
                } else {
                    call.respondText("Data sensor dengan ID $id tidak ditemukan", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Parameter ID tidak valid", status = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }

    suspend fun deleteDataById(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                dbHandler.deleteDataById(id)
                call.respondText("Data sensor dengan ID $id berhasil dihapus")
            } else {
                call.respondText("Parameter ID tidak valid", status = HttpStatusCode.BadRequest)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
        }
    }
}
