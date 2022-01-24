package com.example.asosspacex.ui.viewmodels

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.asosspacex.R
import com.example.asosspacex.events.FiltersAppliedEvent
import com.example.asosspacex.models.defaultFilters
import com.example.asosspacex.ui.adapters.YearFilterAdapter
import com.example.asosspacex.ui.viewmodels.listitems.YearFilterListItemViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    val adapter: YearFilterAdapter,
    private val eventBus: EventBus
): ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private var currentFilters = defaultFilters
    private var allYears: ArrayList<Int> = arrayListOf()

    val selectedOrderingButtonId = ObservableField(R.id.order_ascending)
    val selectedSuccessButtonId = ObservableField(R.id.all)
    val filtersVisible = ObservableField(false)
    val columns = ObservableField(4)

    fun orderingRadioButtonsClicked(v: View) {
        when(v.id) {
            R.id.order_ascending -> currentFilters.ascendingOrder = true
            R.id.order_descending -> currentFilters.ascendingOrder = false
        }
    }

    fun successRadioButtonsClicked(v: View) {
        when(v.id) {
            R.id.success -> currentFilters.successFilter = SuccessFilter.SUCCESS_ONLY
            R.id.failure -> currentFilters.successFilter = SuccessFilter.FAILURE_ONLY
            R.id.all -> currentFilters.successFilter = SuccessFilter.ALL
        }

    }

    fun applyFilters(v: View) {
        setExcludedYears(adapter.getExcludedYears())
        eventBus.post(FiltersAppliedEvent(currentFilters))
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setExcludedYears(excluded: ArrayList<Int>) {
        currentFilters.excludeYears = excluded
    }

    fun updateYearsList(years: ArrayList<Int>) {
        allYears.clear()
        allYears.addAll(years)

        val yearFilterListViewModels: ArrayList<YearFilterListItemViewModel> = arrayListOf()
        years.forEach {
            yearFilterListViewModels.add(
                YearFilterListItemViewModel(it, !currentFilters.excludeYears.contains(it))
            )
        }
        adapter.setYears(yearFilterListViewModels)
    }
}

enum class SuccessFilter {
    ALL, SUCCESS_ONLY, FAILURE_ONLY
}