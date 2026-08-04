package com.example.todotasks.ui.model

import android.os.Parcelable
import com.example.todotasks.domain.model.TaskPriority
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubTaskUI(
    val id: Long = 0,
    val idTask: Long = 0,
    val title: String = "",
    val completed: Boolean = false,
    val priority: TaskPriority = TaskPriority.NORMAL
): Parcelable