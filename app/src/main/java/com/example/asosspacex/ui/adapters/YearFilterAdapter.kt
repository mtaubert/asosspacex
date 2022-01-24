package com.example.asosspacex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.asosspacex.BR
import com.example.asosspacex.R
import com.example.asosspacex.ui.viewmodels.listitems.YearFilterListItemViewModel
import javax.inject.Inject

class YearFilterAdapter @Inject constructor(
    private val adapterHelper: AdapterHelper
): RecyclerView.Adapter<YearFilterAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemViewModel: YearFilterListItemViewModel) {
            binding.setVariable(BR.viewModel, itemViewModel)
        }
    }

    private val years: ArrayList<YearFilterListItemViewModel> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_year_filter,
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(years[position])
    }

    override fun getItemCount() = years.size

    fun setYears(years: ArrayList<YearFilterListItemViewModel>) {
        this.years.clear()
        this.years.addAll(years)
        adapterHelper.notifyDataSetChanged(this)
    }

    fun getExcludedYears(): ArrayList<Int> {
        val excludedYears = arrayListOf<Int>()
        years.forEach {
            if(!it.isChecked.get()!!) excludedYears.add(it.year)
        }
        return excludedYears
    }
}