package com.example.asosspacex.models

import com.example.asosspacex.ui.viewmodels.SuccessFilter

val defaultFilters = Filters(
    true,
    SuccessFilter.ALL,
    arrayListOf()
)

data class Filters(
    var ascendingOrder: Boolean,
    var successFilter: SuccessFilter,
    var excludeYears: ArrayList<Int>
) {
}