package com.example.todotasks.domain.model


import kotlinx.serialization.Serializable

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Serializable
data class GroupAndSortTask(
    val groupBy: GroupByTask = GroupByTask.PRIORITY,
    val sortBy: SortByTask = SortByTask.TITTLE,
    val sortOrder: SortOrder = SortOrder.ASC
)

@Serializable
data class GroupAndSortSubTask(
    val groupBy: GroupBySubTask = GroupBySubTask.PRIORITY,
    val sortBy: SortBySubTask = SortBySubTask.TITTLE,
    val sortOrder: SortOrder = SortOrder.ASC
)

@Serializable
enum class GroupByTask() {
    PRIORITY,
    DATE
}

@Serializable
enum class GroupBySubTask() {
    PRIORITY
}

@Serializable
enum class SortByTask() {
    TITTLE,
    PRIORITY,
    DATE
}

@Serializable
enum class SortBySubTask() {
    TITTLE,
    PRIORITY
}

@Serializable
enum class SortOrder(){
    ASC,
    DESC
}