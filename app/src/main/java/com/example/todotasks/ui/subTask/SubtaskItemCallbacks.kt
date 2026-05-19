package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.TaskPriority

data class SubtaskItemCallbacks(
    val updateSubTaskCompleted: (Long, Boolean) -> Unit,
    val updateSubTaskName: (Long, String, TaskPriority) -> Unit,
    val deleteSubtask: (Long, String) -> Unit
)