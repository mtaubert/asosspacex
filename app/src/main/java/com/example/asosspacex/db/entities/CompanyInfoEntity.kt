package com.example.asosspacex.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_info")
data class CompanyInfoEntity(
    @PrimaryKey val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    val launch_sites: Int,
    val valuation: Long,
    val dt: Long
)