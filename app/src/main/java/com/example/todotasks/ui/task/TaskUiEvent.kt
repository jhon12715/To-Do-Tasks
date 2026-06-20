package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority
import java.time.LocalDate

/**
 * Project: To Do Tasks
 * Created by: Jhon

 */

sealed class TaskUiEvent{

    //Activity
    data class UpdateTaskFilter(val taskFilter: TaskFilter): TaskUiEvent()
    data class UpdateCategoryTaskSelected(val categorySelectedId: Long): TaskUiEvent()
    data class UpdateCompletedTask(val taskId: Long, val isCompleted: Boolean): TaskUiEvent()
    data class DeletedTask(val taskId: Long): TaskUiEvent()
    data object OpenNewTaskDialog: TaskUiEvent()
    data class OpenEditTaskDialog(val taskId: Long, val taskName: String, val taskPriority: TaskPriority, val taskDate: LocalDate?, val categoryId: Long?): TaskUiEvent()
    //FormDialog
    data class UpdateNameTaskForm(val taskForm: String, val idForm: Long): TaskUiEvent()
    data class UpdateDateTaskForm(val dateForm: LocalDate?): TaskUiEvent()
    data class UpdateCategoryIdTaskForm(val categoryFormSelectedId: Long?): TaskUiEvent()
    data class UpdatePriorityTaskForm(val taskPriorityForm: TaskPriority): TaskUiEvent()
    data object UpsertTaskForm: TaskUiEvent()
    data class UpsertCategory(val name: String): TaskUiEvent()
}