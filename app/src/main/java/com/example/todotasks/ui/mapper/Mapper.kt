package com.example.todotasks.ui.mapper

import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskListItem
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI

fun List<Category>.toUi(categorySelected: Long): List<TypeCategoryUI> {

    val allCategory = listOf<TypeCategoryUI.CategoryItem>(
        TypeCategoryUI.CategoryItem(
            CategoryUI(id = -1L, name = "Todas"),
            selected = categorySelected == -1L
        )
    )

    return allCategory +
            map {
                val selected = it.id == categorySelected
                TypeCategoryUI.CategoryItem(it.toUI(), selected)
            } + TypeCategoryUI.AddCategory
}

fun List<Category>.toFormUi(): List<CategoryUI> {

    val noCategory = CategoryUI(id = -1L, name = "Sin categoría")
println("lista: ${listOf(noCategory)  + this.map { it.toUI()}}")
    return listOf(noCategory)  + this.map { it.toUI() }
}

fun TaskListItem.toUi(): TaskUI = TaskUI(
    id = task.id,
    tittle = task.tittle,
    completedSubtask = completedSubtask,
    totalSubTask = totalSubTask,
    isCompleted = task.isCompleted,
    priority = task.priority,
    date = task.date,
    categoryId = task.categoryId,
)

fun TaskUI.toDomain(): Task = Task(
    id = id,
    tittle = tittle,
    priority = priority,
    isCompleted = isCompleted,
    date = date,
    categoryId = categoryId
)

fun Category.toUI(): CategoryUI = CategoryUI(
    id = id,
    name = name
)

fun CategoryUI.toDomain(): Category = Category(
    id = id,
    name = name
)