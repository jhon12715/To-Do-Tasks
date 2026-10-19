package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.domain.model.SortOrder
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.core.sortByOrder
import com.example.todotasks.ui.model.DateHeaderSorter
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeTaskListItem
import java.time.LocalDate
import java.time.temporal.ChronoUnit


fun List<TaskUI>.filterByIsCompleted(filterIsCompleted: IsCompletedFilter): List<TaskUI> {

    return when (filterIsCompleted) {
        IsCompletedFilter.ALL -> this
        IsCompletedFilter.COMPLETED -> this.filter { it.isCompleted }
        IsCompletedFilter.NOT_COMPLETED -> this.filter { !it.isCompleted }
    }
}

//fun List<TaskUI>.applyGroupByAndOrderBy(filter: TaskFilter): List<TaskUI> {}

fun List<TaskUI>.applyTaskGroupByAndSorterBy(
    groupByTask: GroupByTask,
    sortByTask: SortByTask,
    sortOrder: SortOrder
): List<TypeTaskListItem> {
    //GroupBy
    fun groupByDateHeader(date: LocalDate?): String {
        if (date == null) return DateHeaderSorter.SIN_FECHA.header
        val today = LocalDate.now()
        val dateDiference = ChronoUnit.DAYS.between(today, date)
        return when {
            dateDiference == 0L -> DateHeaderSorter.HOY.header
            dateDiference > 0L -> DateHeaderSorter.FUTURAS.header
            else -> DateHeaderSorter.VENCIDAS.header
        }
    }

    fun sortedPriorityMapOrder(priorityHeader: String): Int =
        TaskPriority.valueOf(priorityHeader).num

    fun sortedDateMapOrder(dateHeader: String): Int {
        val dateSorter = DateHeaderSorter.entries.find { it.header == dateHeader }
        return dateSorter!!.num
    }


    fun List<TaskUI>.getListGrouped(): Map<String, List<TaskUI>> {
        return when (groupByTask) {
            GroupByTask.PRIORITY -> this.groupBy { it.priority.toString() }.toSortedMap(
                compareBy { sortedPriorityMapOrder(it) }
            )

            GroupByTask.DATE -> this.groupBy { groupByDateHeader(it.date) }.toSortedMap(
                compareBy { sortedDateMapOrder(it) }
            )
        }
    }

    //SorterBy

    fun sortByDate(date: LocalDate?): Long {
        val today = LocalDate.now()
        return when {
            date == null -> Long.MAX_VALUE

            date.isBefore(today) ->
                // vencidas: más reciente primero
                ChronoUnit.DAYS.between(date, today)

            else ->
                // futuras: más cercanas primero
                ChronoUnit.DAYS.between(today, date)
        }
    }

    fun Map<String, List<TaskUI>>.getListSorted(): Map<String, List<TaskUI>> {
        return when (sortByTask) {
            SortByTask.TITTLE -> this.mapValues {
                it.value.sortByOrder(
                    sortOrder,
                    { task -> task.tittle.lowercase() },
                    { task -> task.id })

            }

            SortByTask.PRIORITY -> this.mapValues {
                it.value.sortByOrder(
                    sortOrder,
                    { task -> task.priority },
                    { task -> task.id })
            }

            SortByTask.DATE -> this.mapValues {
                it.value.sortByOrder(
                    sortOrder,
                    { task -> sortByDate(task.date) },
                    { task -> task.id })
            }
        }
    }

    val groupedItem: Map<String, List<TaskUI>> = this.getListGrouped()

    val sortedItem: Map<String, List<TaskUI>> = groupedItem.getListSorted()

    val taskGroupedAndSortedItem: List<TypeTaskListItem> = sortedItem.flatMap { (clave, task) ->
        val claveItem = TypeTaskListItem.HeaderItem(clave)
        val taskItem = task.map { it.toTaskItem() }

        listOf(claveItem) + taskItem
    }

    return taskGroupedAndSortedItem

}

fun TaskUI.toTaskItem(): TypeTaskListItem.TaskItem {
    return TypeTaskListItem.TaskItem(this)
}

fun List<TaskUI>.applyTaskFilterAndSort(filter: IsCompletedFilter): List<TaskUI> {

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

        IsCompletedFilter.ALL ->
            sortedWith(
                compareBy<TaskUI> { it.isCompleted }
                    .thenBy { it.priority.num }
                    .thenBy { dateGroup(it) }
                    .thenBy { dateOrder(it) }
                    .thenBy { it.id }
            )

        IsCompletedFilter.NOT_COMPLETED ->
            filter { !it.isCompleted }
                .sortedWith(
                    compareBy<TaskUI> { it.priority.num }
                        .thenBy { dateGroup(it) }
                        .thenBy { dateOrder(it) }
                        .thenBy { it.id }
                )

        IsCompletedFilter.COMPLETED ->
            filter { it.isCompleted }
                .sortedBy { it.id }
    }
}