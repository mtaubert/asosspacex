package com.example.asosspacex.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class AdapterHelper @Inject constructor() {
    fun notifyDataSetChanged(adapter: LaunchesAdapter) {
        adapter.notifyDataSetChanged()
    }

    fun notifyDataSetChanged(adapter: YearFilterAdapter) {
        adapter.notifyDataSetChanged()
    }
}