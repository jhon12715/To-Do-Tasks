package com.example.todotasks.ui.model

import android.os.Parcelable
import com.example.todotasks.domain.model.TaskPriority
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubTaskUI(
    val id: Long = 0,
    val idTask: Long  =0,
    val tittle: String = "",
    val isCompleted: Boolean = false,
    val isSelectedToDelete: Boolean = false,
    val priority: TaskPriority = TaskPriority.NORMAL
): Parcelable