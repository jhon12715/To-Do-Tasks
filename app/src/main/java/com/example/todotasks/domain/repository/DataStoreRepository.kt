package com.example.todotasks.domain.repository

import com.example.todotasks.domain.model.GroupAndSortSubTask
import com.example.todotasks.domain.model.GroupAndSortTask
import kotlinx.coroutines.flow.Flow

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

interface DataStoreRepository{
    fun getTaskGroupAndSort(): Flow<GroupAndSortTask>
    suspend fun setTaskGroupAndSort(groupAndSort: GroupAndSortTask)

    fun getSubTaskGroupAndSort(): Flow<GroupAndSortSubTask>
    suspend fun setSubTaskGroupAndSort(groupAndSort: GroupAndSortSubTask)
}