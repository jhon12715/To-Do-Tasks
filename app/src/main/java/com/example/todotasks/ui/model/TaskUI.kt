package com.example.todotasks.ui.model

import android.os.Parcelable
import com.example.todotasks.domain.model.TaskPriority
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TaskUI(
    val id: Long = 0,
    val task: String = "",
    val completedSubtask: Int = 0,
    val totalSubTask: Int = 0,
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val date: LocalDate? = null,
    val categoryId: Long? = null
) : Parcelable {
    val subTaskCompleted: String get() = "$completedSubtask/$totalSubTask"
}