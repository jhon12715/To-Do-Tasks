package com.example.todotasks.ui.subTask.subtask_list

import com.example.todotasks.domain.model.IsCompletedFilter

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class SubTaskUiEvent() {
    data class UpdateFilterSubTaskCompleted(val filterCompleted: IsCompletedFilter) : SubTaskUiEvent()
    data class UpdateSubTaskCompleted(val subTaskId: Long, val isCompleted: Boolean) : SubTaskUiEvent()
    data class AddSubTaskToDeleteList(val subTaskId: Long) : SubTaskUiEvent()
    data class RemoveSubTaskToDeleteList(val subTaskId: Long) : SubTaskUiEvent()
    data object CommitSubTaskDelete: SubTaskUiEvent()
    data object ExitFromDeleteMode: SubTaskUiEvent()
}