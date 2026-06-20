package com.example.todotasks.data.mapper

import com.example.todotasks.data.database.entities.CategoryEntity
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity
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
    task = task,
    priority = priority,
    isCompleted = isCompleted,
    date = date,
    categoryId = category
)

fun List<TaskEntity>.toDomain(): List<Task> =
    map { it.toDomain() }

fun Task.toEntity() = TaskEntity(
    id = id,
    task = task,
    isCompleted = isCompleted,
    priority = priority,
    date = date,
    category = categoryId
)

//SubTask
fun SubTaskEntity.toDomain() = SubTask(
    id = id,
    idTask = idTask,
    title = title,
    completed = completed,
    priority = priority
)

fun SubTask.toEntity() = SubTaskEntity(
    id = id,
    idTask = idTask,
    title = title,
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