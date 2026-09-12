package com.example.todotasks.data.repository

import com.example.todotasks.data.local.datastore.DataStoreSource
import com.example.todotasks.domain.model.GroupAndSort
import com.example.todotasks.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DataStoreRepositoryImpl @Inject constructor(private val dataStoreSource: DataStoreSource): DataStoreRepository {
    override fun getTaskGroupAndSort(): Flow<GroupAndSort> = dataStoreSource.getTaskGroupAndSort()


    override suspend fun setTaskGroupAndSort(groupAndSort: GroupAndSort) {
        dataStoreSource.setTaskGroupAndSort(groupAndSort)
    }
}