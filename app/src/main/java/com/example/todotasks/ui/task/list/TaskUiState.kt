package com.example.todotasks.ui.task.list

import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */
data class TaskUiState (
    val tasks: List<TaskUI> = emptyList(),
    val categories: List<TypeCategoryUI> = emptyList(),
    val selectedFilterCategoryId: Long = -1,
    val selectedFilterCompletedTask: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = true
)