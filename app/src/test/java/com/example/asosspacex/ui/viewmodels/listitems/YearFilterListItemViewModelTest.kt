package com.example.asosspacex.ui.viewmodels.listitems

import junit.framework.TestCase.assertEquals
import org.junit.Test

class YearFilterListItemViewModelTest {

    @Test
    fun onInit_valuesCorrect() {
        val subject = YearFilterListItemViewModel(2022, true)

        assertEquals("2022", subject.yearText.get())
        assertEquals(true, subject.isChecked.get())
    }
}