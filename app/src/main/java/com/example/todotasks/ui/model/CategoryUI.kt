package com.example.todotasks.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Parcelize
data class CategoryUI(
    val id: Long = 0,
    val name: String = ""
): Parcelable