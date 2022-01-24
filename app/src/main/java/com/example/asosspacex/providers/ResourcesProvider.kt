package com.example.asosspacex.providers

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }

    fun getDrawable(drawableRes: Int): Drawable? {
        return context.getDrawable(drawableRes)
    }
}