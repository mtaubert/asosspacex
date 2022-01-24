package com.example.asosspacex.networking.responses

import com.example.asosspacex.models.Links

data class LaunchesResponse(
    val id: String,
    val links: Links,
    val name: String,
    val date_unix: Long,
    val upcoming: Boolean,
    val rocket: String,
    val success: Boolean
)
