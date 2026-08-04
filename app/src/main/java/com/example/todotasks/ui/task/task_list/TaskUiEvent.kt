package com.example.todotasks.ui.task.task_list

import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.ui.model.CategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class TaskUiEvent {
    data class UpdateTaskFilter(val taskIsCompletedFilter: TaskIsCompletedFilter) : TaskUiEvent()
    data class UpdateCategoryTaskSelected(val categorySelectedId: Long) : TaskUiEvent()
    data class UpdateCompletedTask(val taskId: Long, val isCompleted: Boolean) : TaskUiEvent()
    data class DeleteTask(val taskId: Long) : TaskUiEvent()
    data class DeleteCategory(val  category: CategoryUI): TaskUiEvent()
    data class UpdateGroupBy(val  groupBy: GroupByTask): TaskUiEvent()
    data class UpdateSortBy(val  sortBy: SortByTask): TaskUiEvent()
}