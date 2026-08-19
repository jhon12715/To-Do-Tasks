package com.example.todotasks.ui.subTask.subtask_list

import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TypeSubTaskListItem

data class SubTaskUiState(
    val tittle: String = "",
    val filterSelected: IsCompletedFilter = IsCompletedFilter.ALL,
    val listSubtasks: List<TypeSubTaskListItem> = emptyList(),
    val subTasksCompleted: Int = 0,
    val subTaskTotal: Int = 0,
    val screenMode: ScreenMode = ScreenMode.LOADING,
    val subTaskSelectedToDelete: Int = 0
) {
    val subTaskCompletedText: String get() = "$subTasksCompleted/$subTaskTotal"
    val subTaskToDeleteText: String get() = "$subTaskSelectedToDelete/$subTaskTotal"
}