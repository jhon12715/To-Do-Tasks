package com.example.todotasks.ui.subTask.subtask_form

import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.SubTaskUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class SubTaskFormEvent(){
    data class OpeningForm(val subTask: SubTaskUI): SubTaskFormEvent()
    data class UpdateSubTaskName(val name: String): SubTaskFormEvent()
    data class UpdateSubTaskPriority(val priority: TaskPriority): SubTaskFormEvent()
    data object CommitForm: SubTaskFormEvent()
}