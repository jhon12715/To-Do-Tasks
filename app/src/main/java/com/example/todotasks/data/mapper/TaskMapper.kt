package com.example.todotasks.data.mapper

import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task

//Task
fun TaskEntity.toDomain() = Task(
    id = id,
    task = task,
)

fun List<TaskEntity>.toDomain(): List<Task> =
    map { it.toDomain() }

fun Task.toEntity() = TaskEntity(
    id = id,
    task = task
)

//SubTask
fun SubTaskEntity.toDomain() = SubTask(
    id = id,
    idTask = idTask,
    title = title,
    completed = completed
)

fun SubTask.toEntity() = SubTaskEntity(
    id = id,
    idTask = idTask,
    title = title,
    completed = completed
)