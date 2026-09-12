package com.example.todotasks.data.mapper

import com.example.todotasks.data.local.room.entities.CategoryEntity
import com.example.todotasks.data.local.room.entities.SubTaskEntity
import com.example.todotasks.data.local.room.entities.TaskEntity
import com.example.todotasks.data.projection.TaskWithStatsProjection
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskListItem

//Task
fun TaskWithStatsProjection.toDomain() = TaskListItem(
    task = task.toDomain(),
    completedSubtask = completedSubtask,
    totalSubTask = totalSubTask
)

fun TaskEntity.toDomain() = Task(
    id = id,
    tittle = tittle,
    priority = priority,
    isCompleted = isCompleted,
    date = date,
    categoryId = categoryId
)

fun List<TaskEntity>.toDomain(): List<Task> =
    map { it.toDomain() }

fun Task.toEntity() = TaskEntity(
    id = id,
    tittle = tittle,
    isCompleted = isCompleted,
    priority = priority,
    date = date,
    categoryId = categoryId
)

//SubTask
fun SubTaskEntity.toDomain() = SubTask(
    id = id,
    idTask = idTask,
    tittle = title,
    completed = completed,
    priority = priority
)

fun SubTask.toEntity() = SubTaskEntity(
    id = id,
    idTask = idTask,
    title = tittle,
    completed = completed,
    priority = priority
)

//Category

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name
)