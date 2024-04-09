package com.basyair7

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection


class DatabaseHandler {
    init {
        Database.connect("jdbc:sqlite:sensor_data.db", driver = "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction {
            SchemaUtils.create(SensorData)
        }
    }

    fun saveData(temperature: Double, humidity: Double): Int {
        var id: Int = -1
        transaction {
            SensorData.insert {
                it[SensorData.temperature] = temperature
                it[SensorData.humidity] = humidity
            }
            id = SensorData.select {
                SensorData.temperature eq temperature and
                        (SensorData.humidity eq humidity)
            }.single()[SensorData.id]
        }
        return id
    }

    fun findAllData(): List<SensorDataModel> {
        var sensorDataList = listOf<SensorDataModel>()
        transaction {
            sensorDataList = SensorData.selectAll().map {
                SensorDataModel.fromResultRow(it)
            }
        }
        return sensorDataList
    }

    fun findDataById(id: Int): SensorDataModel? {
        var sensorData: SensorDataModel? = null
        transaction {
            val result = SensorData.select {
                SensorData.id eq id
            }.singleOrNull()
            if (result != null) {
                sensorData = SensorDataModel.fromResultRow(result)
            }
        }
        return sensorData
    }

    fun updateData(id: Int, temperature: Double, humidity: Double) {
        transaction {
            SensorData.update({ SensorData.id eq id }) {
                it[SensorData.temperature] = temperature
                it[SensorData.humidity] = humidity
            }
        }
    }

    fun deleteDataById(id: Int) {
        transaction {
            SensorData.deleteWhere { SensorData.id eq id }
        }
    }
}

object SensorData : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val temperature = double("temperature")
    val humidity = double("humidity")
}

data class SensorDataModel(val id: Int, val temperature: Double, val humidity: Double) {
    companion object {
        fun fromResultRow(row: ResultRow): SensorDataModel {
            return SensorDataModel (
                id = row[SensorData.id],
                temperature = row[SensorData.temperature],
                humidity = row[SensorData.humidity]
            )
        }
    }
}