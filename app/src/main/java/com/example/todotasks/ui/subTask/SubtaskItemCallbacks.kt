package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority

data class SubtaskItemCallbacks(
    val updateSubTaskCompleted: (Long, Boolean) -> Unit,
    val editSubTaskForm: (SubTask) -> Unit,
    val deleteSubtask: (Long, String) -> Unit
)