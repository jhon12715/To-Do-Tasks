package com.example.todotasks.domain.model


import kotlinx.serialization.Serializable

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Serializable
data class TaskListPreferences(
    val groupAndSort: GroupAndSort = GroupAndSort(),
    val isCompletedFilter: TaskIsCompletedFilter = TaskIsCompletedFilter.ALL
)

@Serializable
enum class TaskIsCompletedFilter{
    ALL,
    COMPLETED,
    NOT_COMPLETED
}

@Serializable
data class GroupAndSort(
    val groupBy: GroupByTask = GroupByTask.PRIORITY,
    val sortBy: SortByTask = SortByTask.TITTLE,
    val sortOrder: SortOrder = SortOrder.ASC
)

@Serializable
enum class GroupByTask() {
    PRIORITY,
    DATE
}

@Serializable
enum class SortByTask() {
    TITTLE,
    PRIORITY,
    DATE
}

@Serializable
enum class SortOrder(){
    ASC,
    DESC
}