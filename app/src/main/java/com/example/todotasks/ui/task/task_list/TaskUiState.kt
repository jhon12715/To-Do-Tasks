package com.example.todotasks.ui.task.task_list

import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */
data class TaskUiState (
    val tasks: List<TaskUI> = emptyList(),
    val categories: List<TypeCategoryUI> = emptyList(),
    val groupBySelected: GroupByTask = GroupByTask.DATE,
    val orderBySelected: SortByTask = SortByTask.TITTLE,
    val selectedFilterCategoryId: Long = -1,
    val isLoading: Boolean = true
)