package com.basyair7.plugins

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.LocalDateTime

class DatabaseHandler {
    init {
        createDatabase()
    }

    fun saveData(id: Int = 0, temperature: Double, humidity: Double): SensorDataModel? {
        createDatabase()
        var sensorData: SensorDataModel? = null
        var idPost: Int
        val now = LocalDateTime.now()
        val datetime = "${now.dayOfMonth}/${now.monthValue}/${now.year} (${now.hour}.${now.minute}.${now.second})"
        transaction {
            if (id != 0) {
                SensorData.insert {
                    it[SensorData.id] = id
                    it[SensorData.temperature] = temperature
                    it[SensorData.humidity] = humidity
                    it[createDate] = datetime
                }
            }
            else {
                SensorData.insert {
                    it[SensorData.temperature] = temperature
                    it[SensorData.humidity] = humidity
                    it[createDate] = datetime
                }
            }

            // see result data if success post
            idPost = SensorData.select {
                (SensorData.temperature eq temperature) and (SensorData.humidity eq humidity)
            }.single()[SensorData.id]

            val result = SensorData.select {
                SensorData.id eq idPost
            }.singleOrNull()
            if (result != null) {
                sensorData = SensorDataModel.fromResultRow(result)
            }
        }
        return sensorData
    }

    fun findAllData(): List<SensorDataModel> {
        createDatabase()
        var sensorDataList = listOf<SensorDataModel>()
        transaction {
            sensorDataList = SensorData.selectAll().map {
                SensorDataModel.fromResultRow(it)
            }
        }
        return sensorDataList
    }

    fun findDataById(id: Int): SensorDataModel? {
        createDatabase()
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

    fun updateData(id: Int, temperature: Double, humidity: Double): SensorDataModel? {
        createDatabase()
        var sensorData: SensorDataModel? = null
        val now = LocalDateTime.now()
        val datetime = "${now.dayOfMonth}/${now.monthValue}/${now.year} (${now.hour}.${now.minute}.${now.second})"
        transaction {
            SensorData.update({ SensorData.id eq id }) {
                it[SensorData.temperature] = temperature
                it[SensorData.humidity] = humidity
                it[updateDate] = datetime
            }

            // see result data if success post
            val result = SensorData.select {
                SensorData.id eq id
            }.singleOrNull()
            if (result != null) {
                sensorData = SensorDataModel.fromResultRow(result)
            }
        }
        return sensorData
    }

    fun deleteDataById(id: Int) {
        createDatabase()
        transaction {
            SensorData.deleteWhere { SensorData.id eq id }
        }
    }

    private fun createDatabase() {
        Database.connect("jdbc:sqlite:sensor_data.db", driver = "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction {
            SchemaUtils.create(SensorData)
        }
    }
}

object SensorData : Table() {
    val id = this.integer("id").autoIncrement().primaryKey()
    val temperature = this.double("temperature")
    val humidity = this.double("humidity")
    val createDate = this.text("createDate").default("").nullable()
    val updateDate = this.text("updateDate").default("").nullable()
}

data class SensorDataModel(val id: Int, val temperature: Double, val humidity: Double, val createDate: String?, val updateDate: String?)
{
    companion object {
        fun fromResultRow(row: ResultRow): SensorDataModel {
            return SensorDataModel (
                id = row[SensorData.id],
                temperature = row[SensorData.temperature],
                humidity = row[SensorData.humidity],
                createDate = row[SensorData.createDate],
                updateDate = row[SensorData.updateDate]
            )
        }
    }
}