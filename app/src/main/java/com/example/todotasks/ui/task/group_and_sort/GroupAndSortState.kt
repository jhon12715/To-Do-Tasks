package com.example.todotasks.ui.task.group_and_sort

import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.SortOrder

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

data class GroupAndSortState(
    val groupBy: GroupByTask = GroupByTask.PRIORITY,
    val sortBy: SortByTask = SortByTask.TITTLE,
    val sortOrder: SortOrder = SortOrder.ASC
)