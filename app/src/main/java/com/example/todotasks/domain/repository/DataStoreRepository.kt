package com.example.todotasks.domain.repository

import com.example.todotasks.domain.model.GroupAndSort
import kotlinx.coroutines.flow.Flow

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

interface DataStoreRepository{
    fun getTaskGroupAndSort(): Flow<GroupAndSort>
    suspend fun setTaskGroupAndSort(groupAndSort: GroupAndSort)
}