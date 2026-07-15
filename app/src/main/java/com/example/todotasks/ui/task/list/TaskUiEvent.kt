package com.example.todotasks.ui.task.list

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.CategoryUI
import java.time.LocalDate

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class TaskUiEvent {
    data class UpdateTaskFilter(val taskFilter: TaskFilter) : TaskUiEvent()
    data class UpdateCategoryTaskSelected(val categorySelectedId: Long) : TaskUiEvent()
    data class UpdateCompletedTask(val taskId: Long, val isCompleted: Boolean) : TaskUiEvent()
    data class DeleteTask(val taskId: Long) : TaskUiEvent()
    data class DeleteCategory(val  category: CategoryUI): TaskUiEvent()
}