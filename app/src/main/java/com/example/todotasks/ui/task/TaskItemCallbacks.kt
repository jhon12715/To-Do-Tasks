package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.TaskUI
import java.time.LocalDate

data class TaskItemCallbacks (
    val editTask: (TaskUI) -> Unit,
    val openSubTaskActivity: (Long, String) -> Unit,
    val deleteTask: (Task) -> Unit,
    val updateCompletedTask: (Long, Boolean) -> Unit
)