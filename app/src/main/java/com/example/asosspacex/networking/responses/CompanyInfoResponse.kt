package com.example.asosspacex.networking.responses

data class CompanyInfoResponse(
    val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    val launch_sites: Int,
    val valuation: Long
)