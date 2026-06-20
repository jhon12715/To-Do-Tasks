package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import java.time.LocalDate

data class TaskItemCallbacks (
    val editTask: (Long, String, TaskPriority, LocalDate?, Long?) -> Unit,
    val openSubTaskActivity: (Long, String) -> Unit,
    val deleteTask: (Task) -> Unit,
    val updateCompletedTask: (Long, Boolean) -> Unit
)