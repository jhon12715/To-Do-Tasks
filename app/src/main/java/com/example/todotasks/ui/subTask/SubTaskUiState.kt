package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskFilter

data class SubTaskUiState(
    val title: String = "",
    val filterSelected: TaskFilter = TaskFilter.ALL,
    val listSubtasks: List<SubTask> = emptyList(),
    val subTasksCompleted: Int = 0,
    val subTaskTotal: Int = 0,
    val isFormVisible: Boolean = false
)