package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.model.TypeSubTaskListItem

fun List<SubTask>.applySubTaskFilterAndSort(filter: TaskIsCompletedFilter): List<TypeSubTaskListItem> {

    fun List<SubTask>.listFiltered(): List<SubTask> {
        return when (filter) {

            TaskIsCompletedFilter.ALL ->
                sortedBy { it.id }

            TaskIsCompletedFilter.COMPLETED ->
                filter { it.completed }
                    .sortedBy { it.id }

            TaskIsCompletedFilter.NOT_COMPLETED ->
                filter { !it.completed }
                    .sortedBy { it.id }
        }
    }

    fun List<SubTask>.listGroupedBy(): Map<TaskPriority, List<SubTask>> {
        return this.groupBy { it.priority }.toSortedMap(compareBy() { it.num })
    }

    val listFiltered = this.listFiltered()
    val listfilteredAndGrouped: Map<TaskPriority, List<SubTask>> = listFiltered.listGroupedBy()


    val listflated = listfilteredAndGrouped.flatMap {
        val header = listOf(TypeSubTaskListItem.HeaderItem(it.key.name))
        val items = it.value.map {
            TypeSubTaskListItem.SubTaskItem(
                it.toUI()
            )
        }
        header + items

    }
    return listflated
}

fun SubTaskUI.toDomain(): SubTask {
    return SubTask(
        id = this.id,
        idTask = this.idTask,
        title = this.title,
        completed = this.completed,
        priority = this.priority
    )
}

fun SubTask.toUI(): SubTaskUI {
    return SubTaskUI(
        id = this.id,
        idTask = this.idTask,
        title = this.title,
        completed = this.completed,
        priority = this.priority
    )
}