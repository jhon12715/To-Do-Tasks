package com.example.todotasks.ui.task.task_form

import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TaskUI
import java.time.LocalDate

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */
sealed class TaskFormEvent {
    data class UpdateNameTaskForm(val taskForm: String): TaskFormEvent()
    data class UpdateDateTaskForm(val dateForm: LocalDate?): TaskFormEvent()
    data class UpdateCategoryIdTaskForm(val categoryFormSelectedId: Long?): TaskFormEvent()
    data class UpdatePriorityTaskForm(val taskPriorityForm: TaskPriority): TaskFormEvent()
    data object UpsertTaskForm: TaskFormEvent()
    data class OpeningForm(val task: TaskUI): TaskFormEvent()
}