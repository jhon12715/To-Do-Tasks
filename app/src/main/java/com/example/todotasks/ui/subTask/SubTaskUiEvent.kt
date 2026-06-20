package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class SubTaskUiEvent() {
    data class UpdateFilterSubTaskCompleted(val filterCompleted: TaskFilter) : SubTaskUiEvent()
    data object CreateNewSubTaskForm : SubTaskUiEvent()
    data class EditSubTaskForm(val subTask: SubTask) : SubTaskUiEvent()
    data class UpdateSubTaskCompleted(val subTaskId: Long, val isCompleted: Boolean) : SubTaskUiEvent()
    data class DeleteSubTask(val subTaskId: Long) : SubTaskUiEvent()

    //Form
    data class ChangeSubTaskNameForm(val newName: String) : SubTaskUiEvent()
    data class ChangeSubTaskPriorityForm(val newPriority: TaskPriority) : SubTaskUiEvent()
    data object UpsertSubTask : SubTaskUiEvent()
    data object CloseForm : SubTaskUiEvent()
}