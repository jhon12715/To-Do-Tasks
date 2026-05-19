package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.ui.task.model.TaskUI
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun List<TaskUI>.applyFilterAndSort(filter: TaskFilter): List<TaskUI> {

    val today = LocalDate.now()

    fun dateGroup(task: TaskUI): Int = when {
        task.date == null -> 2
        task.date.isBefore(today) -> 1
        else -> 0 // hoy y futuras
    }

    fun dateOrder(task: TaskUI): Long {
        return when {
            task.date == null -> Long.MAX_VALUE

            task.date.isBefore(today) ->
                // vencidas: más reciente primero
                ChronoUnit.DAYS.between(task.date, today)

            else ->
                // futuras: más cercanas primero
                ChronoUnit.DAYS.between(today, task.date)
        }
    }
    
    return when (filter) {

        TaskFilter.ALL ->
            sortedWith(
                compareBy<TaskUI> { it.isCompleted }
                    .thenBy { it.priority.num }
                    .thenBy { dateGroup(it) }
                    .thenBy { dateOrder(it) }
                    .thenBy { it.id }
            )

        TaskFilter.NOT_COMPLETED ->
            filter { !it.isCompleted }
                .sortedWith(
                    compareBy<TaskUI> { it.priority.num }
                        .thenBy { dateGroup(it) }
                        .thenBy { dateOrder(it) }
                        .thenBy { it.id }
                )

        TaskFilter.COMPLETED ->
            filter { it.isCompleted }
                .sortedBy { it.id }
    }
}