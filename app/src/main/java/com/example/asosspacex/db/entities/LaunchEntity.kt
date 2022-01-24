package com.example.asosspacex.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.asosspacex.models.Links

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey val id: String,
    val patchLink: String?,
    val wikipediaLink: String?,
    val articleLink: String?,
    val webcastLink: String?,
    val name: String,
    val date_unix: Long,
    val upcoming: Boolean,
    val rocketId: String,
    val success: Boolean,
    val dt: Long
)
