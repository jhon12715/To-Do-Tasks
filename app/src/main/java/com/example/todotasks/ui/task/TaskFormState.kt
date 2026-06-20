package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.TypeCategoryUI
import java.time.LocalDate

/**
 * Project: To Do Tasks
 * Created by: Jhon

 */

data class TaskFormState(
    val id: Long = 0L,
    val taskName: String = "",
    val priority: TaskPriority = TaskPriority.NORMAL,
    val date: LocalDate? = null,
    val categoryId:Long? = null,
    val isValid: Boolean = false,
    val availableCategories: List<Category> = emptyList(),
    val isFormLoading: Boolean = true
)