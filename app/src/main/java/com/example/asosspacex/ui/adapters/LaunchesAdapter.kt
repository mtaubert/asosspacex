package com.example.asosspacex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.asosspacex.BR
import com.example.asosspacex.R
import com.example.asosspacex.models.Filters
import com.example.asosspacex.models.defaultFilters
import com.example.asosspacex.ui.viewmodels.listitems.LaunchListItemViewModel
import com.example.asosspacex.ui.viewmodels.SuccessFilter
import javax.inject.Inject
import kotlin.collections.ArrayList

class LaunchesAdapter @Inject constructor(
    private val adapterHelper: AdapterHelper
): RecyclerView.Adapter<LaunchesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemViewModel: LaunchListItemViewModel) {
            binding.setVariable(BR.viewmodel, itemViewModel)
        }
    }

    private var filters: Filters = defaultFilters
    private val launches: ArrayList<LaunchListItemViewModel> = arrayListOf()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val visibleLaunches: ArrayList<LaunchListItemViewModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_launch_list,
            parent,
            false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(visibleLaunches[position])
    }

    override fun getItemCount(): Int = visibleLaunches.size

    fun setFilters(filters: Filters) {
        this.filters = filters
        filterLaunchesAndShow()
    }

    fun setLaunches(launches: ArrayList<LaunchListItemViewModel>) {
        this.launches.clear()
        this.launches.addAll(launches)
        filterLaunchesAndShow()
    }

    private fun filterLaunchesAndShow() {
        visibleLaunches.clear()

        filterLaunchSuccessAndYear()
        sortByAge()

        adapterHelper.notifyDataSetChanged(this)
    }

    private fun filterLaunchSuccessAndYear() {
        launches.forEach {
            if(launchSuccessMatchesFilter(it.wasSuccessful()) && launchYearNotExcluded(it.launchYear)) {
                visibleLaunches.add(it)
            }
        }
    }

    private fun launchSuccessMatchesFilter(launchSuccessful: Boolean): Boolean {
        return if(filters.successFilter == SuccessFilter.ALL) {
            true
        } else if (launchSuccessful && filters.successFilter == SuccessFilter.SUCCESS_ONLY) {
            true
        } else !launchSuccessful && filters.successFilter == SuccessFilter.FAILURE_ONLY
    }

    private fun launchYearNotExcluded(year: Int): Boolean {
        return !filters.excludeYears.contains(year)
    }

    private fun sortByAge() {
        if(filters.ascendingOrder) {
            visibleLaunches.sortWith { p0, p1 ->
                when {
                    p0.getLaunchTime()> p1.getLaunchTime() -> {
                        1
                    }
                    p0.getLaunchTime() < p1.getLaunchTime() -> {
                        -1
                    }
                    else -> {
                        0
                    }
                }
            }
        } else {
            visibleLaunches.sortWith { p0, p1 ->
                when {
                    p0.getLaunchTime() > p1.getLaunchTime() -> {
                        -1
                    }
                    p0.getLaunchTime() < p1.getLaunchTime() -> {
                        1
                    }
                    else -> {
                        0
                    }
                }
            }
        }
    }
}