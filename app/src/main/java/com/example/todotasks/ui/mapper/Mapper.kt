package com.example.todotasks.ui.mapper

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskListItem
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI

fun List<Category>.toUi(categorySelected: Long): List<TypeCategoryUI> {

    val allCategory = listOf<TypeCategoryUI.CategoryItem>(
        TypeCategoryUI.CategoryItem(
            Category(id = -1L, name = "Todas"),
            selected = categorySelected == -1L
        )
    )

    return allCategory +
            map {
                val selected = it.id == categorySelected
                TypeCategoryUI.CategoryItem(it, selected)
            } + TypeCategoryUI.AddCategory
}

fun List<Category>.toFormUi(): List<Category> {

    val noCategory = Category(id = -1L, name = "Sin categoría")

    return listOf(noCategory)  + this
}

fun TaskListItem.toUi(): TaskUI = TaskUI(
    id = task.id,
    task = task.task,
    completedSubtask = completedSubtask,
    totalSubTask = totalSubTask,
    isCompleted = task.isCompleted,
    priority = task.priority,
    date = task.date,
    categoryId = task.categoryId,
)

fun TaskUI.toDomain(): Task = Task(
    id = id,
    task = task,
    priority = priority,
    isCompleted = isCompleted,
    date = date,
    categoryId = categoryId
)