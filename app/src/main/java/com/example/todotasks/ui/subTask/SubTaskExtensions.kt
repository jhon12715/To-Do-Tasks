package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.model.TypeSubTaskListItem

fun List<SubTaskUI>.applySubTaskFilterAndSort(filter: IsCompletedFilter): List<TypeSubTaskListItem> {

    fun List<SubTaskUI>.listFiltered(): List<SubTaskUI> {
        return when (filter) {

            IsCompletedFilter.ALL -> this

            IsCompletedFilter.COMPLETED ->
                filter { it.isCompleted }

            IsCompletedFilter.NOT_COMPLETED ->
                filter { !it.isCompleted }

        }.sortedBy { it.id }
    }

    fun List<SubTaskUI>.listGrouped(): Map<TaskPriority, List<SubTaskUI>> {
        return this.groupBy { it.priority }.toSortedMap(compareBy() { it.num })
    }


    val filtered = listFiltered()
    val grouped = filtered.listGrouped()


    return grouped.flatMap {(priority, subtasks) ->
        val header = TypeSubTaskListItem.HeaderItem(priority.name)
        val items = subtasks.map { TypeSubTaskListItem.SubTaskItem(it) }

       listOf(header)+ items

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