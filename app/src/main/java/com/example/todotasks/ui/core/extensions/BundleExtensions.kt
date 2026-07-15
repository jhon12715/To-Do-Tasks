package com.example.todotasks.ui.core.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        this.getParcelable(key) as? T
    }
}