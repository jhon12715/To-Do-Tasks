package com.example.todotasks.data.repository

import com.example.todotasks.data.local.datastore.DataStoreSource
import com.example.todotasks.domain.model.GroupAndSortSubTask
import com.example.todotasks.domain.model.GroupAndSortTask
import com.example.todotasks.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DataStoreRepositoryImpl @Inject constructor(private val dataStoreSource: DataStoreSource): DataStoreRepository {
    override fun getTaskGroupAndSort(): Flow<GroupAndSortTask> = dataStoreSource.getTaskGroupAndSortTask()


    override suspend fun setTaskGroupAndSort(groupAndSort: GroupAndSortTask) =
        dataStoreSource.setTaskGroupAndSortTask(groupAndSort)


    override fun getSubTaskGroupAndSort(): Flow<GroupAndSortSubTask> = dataStoreSource.getTaskGroupAndSortSubTask()

    override suspend fun setSubTaskGroupAndSort(groupAndSort: GroupAndSortSubTask) = dataStoreSource.setTaskGroupAndSortSubTask(groupAndSort)
}