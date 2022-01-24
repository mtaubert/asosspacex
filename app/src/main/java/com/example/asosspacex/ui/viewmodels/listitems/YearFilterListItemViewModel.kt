package com.example.asosspacex.ui.viewmodels.listitems

import androidx.databinding.ObservableField

class YearFilterListItemViewModel(val year: Int, checked: Boolean) {
    val yearText = ObservableField("$year")
    val isChecked = ObservableField(checked)
}