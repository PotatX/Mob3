package com.example.gosiandroid10.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Car(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "client") val client: String,
    @ColumnInfo(name = "car_state") val car_state: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
)