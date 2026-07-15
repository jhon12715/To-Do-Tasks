package com.example.todotasks.ui.core.extensions

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.example.todotasks.R

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

fun View.hideKeyboard() {
    val inputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
    inputMethodManager!!.hideSoftInputFromWindow(windowToken, 0)
}

fun View.backgroundMoreWhite(ratio: Float){
    val colorOriginal = ContextCompat.getColor(context, R.color.baseColor)
    val colorClaro = ColorUtils.blendARGB(colorOriginal, Color.WHITE, ratio)
    setBackgroundColor(colorClaro)
}

fun CardView.cardbackgroundMoreWhite(ratio: Float){
    val colorOriginal = ContextCompat.getColor(context, R.color.baseColor)
    val colorClaro = ColorUtils.blendARGB(colorOriginal, Color.WHITE, ratio)
    setCardBackgroundColor(colorClaro)
}