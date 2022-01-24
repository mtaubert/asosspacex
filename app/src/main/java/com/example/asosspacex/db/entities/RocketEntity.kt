package com.example.asosspacex.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rockets")
data class RocketEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val dt: Long
)
