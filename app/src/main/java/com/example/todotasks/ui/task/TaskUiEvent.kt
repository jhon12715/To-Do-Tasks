package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.ScreenMode

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class TaskUiEvent {
    data class UpdateTaskIsCompletedFilter(val taskIsCompletedFilter: IsCompletedFilter) : TaskUiEvent()
    data class UpdateCategoryTaskSelected(val categorySelectedId: Long) : TaskUiEvent()
    data class UpdateCompletedTask(val taskId: Long, val isCompleted: Boolean) : TaskUiEvent()
    data class DeleteCategory(val  category: CategoryUI): TaskUiEvent()
    data class UpdateGroupBy(val  groupBy: GroupByTask): TaskUiEvent()
    data class UpdateSortBy(val  sortBy: SortByTask): TaskUiEvent()
    data class UpdateScreenMode(val screenMode: ScreenMode): TaskUiEvent()
    data class AddTaskToDelete(val taskId: Long): TaskUiEvent()
    data class RemoveTaskToDelete(val taskId: Long): TaskUiEvent()
    data object CommitTaskToDelete: TaskUiEvent()
}