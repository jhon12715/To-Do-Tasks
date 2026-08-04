package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.SubTaskUI

data class SubtaskItemCallbacks(
    val updateSubTaskCompleted: (Long, Boolean) -> Unit,
    val editSubTaskForm: (SubTaskUI) -> Unit,
    val deleteSubtask: (Long, String) -> Unit
)