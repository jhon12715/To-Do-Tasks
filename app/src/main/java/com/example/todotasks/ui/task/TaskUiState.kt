package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */
data class TaskUiState (
    val categories: List<TypeCategoryUI> = emptyList(),
    val selectedFilterCategoryId: Long = -1,
    val selectedFilterCompletedTask: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = true,
    val isFormDialogVisible: Boolean = false
)