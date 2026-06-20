package com.example.todotasks.ui.model

import com.example.todotasks.domain.model.TaskPriority
import java.time.LocalDate

data class TaskUI(val id: Long, val task: String, val completedSubtask: Int, val totalSubTask: Int, val isCompleted: Boolean, val priority: TaskPriority, val date: LocalDate?, val categoryId: Long?){
    val subTaskCompleted: String
        get() = "$completedSubtask/$totalSubTask"
}