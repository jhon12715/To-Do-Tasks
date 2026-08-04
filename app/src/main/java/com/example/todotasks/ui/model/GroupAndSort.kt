package com.example.todotasks.ui.model

import android.os.Parcelable
import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.SortOrder
import kotlinx.parcelize.Parcelize

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@Parcelize
data class GroupAndSort(
    val groupBy: GroupByTask = GroupByTask.PRIORITY,
    val sortBy: SortByTask = SortByTask.TITTLE,
    val sortOrder: SortOrder = SortOrder.ASC
) : Parcelable