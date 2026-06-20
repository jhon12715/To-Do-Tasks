package com.example.todotasks.ui.subTask

import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskFilter

fun List<SubTask>.applySubTaskFilterAndSort(filter: TaskFilter): List<SubTask> {

    return when (filter) {

        TaskFilter.ALL ->
            sortedWith(compareBy({ it.completed },
                { it.priority.num },
                { it.id }))

        TaskFilter.COMPLETED ->
            filter { it.completed }
                .sortedBy { it.id }

        TaskFilter.NOT_COMPLETED ->
            filter { !it.completed }
                .sortedWith(compareBy({ it.priority.num }, { it.id }))
    }
}