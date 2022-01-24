package com.example.asosspacex.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.asosspacex.BR
import com.example.asosspacex.R
import com.example.asosspacex.ui.viewmodels.listitems.LinkListItemViewModel

class LinksAdapter: RecyclerView.Adapter<LinksAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemViewModel: LinkListItemViewModel) {
            binding.setVariable(BR.viewModel, itemViewModel)
        }
    }

    private val links: ArrayList<LinkListItemViewModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_link_list,
            parent,
            false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(links[position])
    }

    override fun getItemCount(): Int = links.size

    fun setLinks(links: List<LinkListItemViewModel>) {
        this.links.clear()
        this.links.addAll(links)
        notifyDataSetChanged()
    }
}