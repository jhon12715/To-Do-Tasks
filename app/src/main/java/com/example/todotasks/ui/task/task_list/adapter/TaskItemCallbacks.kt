package com.example.todotasks.ui.task.task_list.adapter

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import java.time.LocalDate

data class TaskItemCallbacks (
    val editTask: (TaskUI) -> Unit,
    val openSubTaskActivity: (Long, String) -> Unit,
    val updateCompletedTask: (Long, Boolean) -> Unit,
    val updateScreenMode: (ScreenMode) -> Unit,
    val addTaskToDelete: (Long) -> Unit,
    val removeTaskToDelete: (Long) -> Unit
)