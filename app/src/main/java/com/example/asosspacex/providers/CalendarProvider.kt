package com.example.asosspacex.providers

import java.util.*
import javax.inject.Inject

class CalendarProvider @Inject constructor() {
    fun timeInMillis() = Calendar.getInstance().timeInMillis
}