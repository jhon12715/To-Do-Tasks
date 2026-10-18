package com.example.todotasks.ui.more_vert.group_and_sort

import com.example.todotasks.domain.model.GroupBySubTask
import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortBySubTask
import com.example.todotasks.domain.model.SortByTask

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class GroupAndSortEvent() {
    data class UpdateGroupByTask(val groupBy: GroupByTask) : GroupAndSortEvent()
    data class UpdateSortByTask(val sortBy: SortByTask) : GroupAndSortEvent()
    data object UpdateSortOrderTask : GroupAndSortEvent()
    data class UpdateGroupBySubTask(val groupBy: GroupBySubTask) : GroupAndSortEvent()
    data class UpdateSortBySubTask(val sortBy: SortBySubTask) : GroupAndSortEvent()
    data object UpdateSortOrderSubTask : GroupAndSortEvent()
}