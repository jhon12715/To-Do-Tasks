package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.ui.model.TypeSubTaskListItem

data class SubTaskUiState(
    val title: String = "",
    val filterSelected: TaskIsCompletedFilter = TaskIsCompletedFilter.ALL,
    val listSubtasks: List<TypeSubTaskListItem> = emptyList(),
    val subTasksCompleted: Int = 0,
    val subTaskTotal: Int = 0
) {
    val subTaskCompletedText: String get() = "$subTasksCompleted/$subTaskTotal"
}