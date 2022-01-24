package com.example.asosspacex.ui

import android.widget.ImageView

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asosspacex.R
import com.example.asosspacex.di.GlideApp


@BindingAdapter("launchSuccess")
fun setLaunchState(v: ImageView, launchSuccess: Boolean) {
    val drawableRes: Int = if (launchSuccess) R.drawable.ic_check else R.drawable.ic_cross
    v.setImageResource(drawableRes)
}

@BindingAdapter("patchUrl")
fun setPatchUrl(v: ImageView, patchUrl: String?) {
    if (patchUrl != null) {
        GlideApp.with(v.context)
           .load(patchUrl)
           .centerCrop()
           .error(R.drawable.ic_image_missing)
           .into(v)
    } else {
        v.setImageResource(R.drawable.ic_image_missing)
    }
}

@BindingAdapter("linkIcon")
fun setLinkIcon(v: ImageView, drawableRes: Int) {
    v.setImageResource(drawableRes)
}

@BindingAdapter("gridColumns")
fun setGridColumns(v: RecyclerView, columns: Int) {
    v.layoutManager = GridLayoutManager(v.context, columns)
}