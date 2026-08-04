package com.example.todotasks.ui.core.extensions

import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

fun DialogFragment.applyWindowApparence() {
    dialog?.window?.apply {
        setBackgroundDrawableResource(com.example.todotasks.R.drawable.bg_dialog)
        setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(), // ancho 80%
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}