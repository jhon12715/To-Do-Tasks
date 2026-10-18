package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.GroupBySubTask
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.domain.model.SortBySubTask
import com.example.todotasks.domain.model.SortOrder
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.core.sortByOrder
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.model.TypeSubTaskListItem

fun List<SubTaskUI>.applySubTaskFilterAndSort(
    filter: IsCompletedFilter,
    groupBy: GroupBySubTask,
    sortBy: SortBySubTask,
    sortOrder: SortOrder
): List<TypeSubTaskListItem> {

    fun List<SubTaskUI>.listFiltered(): List<SubTaskUI> {
        return when (filter) {

            IsCompletedFilter.ALL -> this

            IsCompletedFilter.COMPLETED ->
                filter { it.isCompleted }

            IsCompletedFilter.NOT_COMPLETED ->
                filter { !it.isCompleted }

        }.sortedBy { it.id }
    }

    fun List<SubTaskUI>.listGrouped(): Map<TaskPriority, List<SubTaskUI>> = when (groupBy) {
        GroupBySubTask.PRIORITY -> this.groupBy { it.priority }.toSortedMap(compareBy() { it.num })
    }

    fun Map<TaskPriority, List<SubTaskUI>>.listSorted(): Map<TaskPriority, List<SubTaskUI>> = when (sortBy) {
        SortBySubTask.TITTLE -> this.mapValues {
            it.value.sortByOrder(sortOrder,
                { subTaskUI -> subTaskUI.tittle },
                { subTaskUI -> subTaskUI.idTask })
        }

        SortBySubTask.PRIORITY -> this.mapValues {
            it.value.sortByOrder(sortOrder,
                { subTaskUI -> subTaskUI.priority },
                { subTaskUI -> subTaskUI.idTask })
        }
    }


    val filtered = listFiltered()
    val grouped = filtered.listGrouped()
    val sorted = grouped.listSorted()

    return sorted.flatMap { (priority, subtasks) ->
        val header = TypeSubTaskListItem.HeaderItem(priority.name)
        val items = subtasks.map { TypeSubTaskListItem.SubTaskItem(it) }

        listOf(header) + items

    }
}

fun SubTaskUI.toDomain(): SubTask {
    return SubTask(
        id = this.id,
        idTask = this.idTask,
        tittle = this.tittle,
        completed = this.isCompleted,
        priority = this.priority
    )
}

fun SubTask.toUI(): SubTaskUI {
    return SubTaskUI(
        id = this.id,
        idTask = this.idTask,
        tittle = this.tittle,
        isCompleted = this.completed,
        priority = this.priority
    )
}