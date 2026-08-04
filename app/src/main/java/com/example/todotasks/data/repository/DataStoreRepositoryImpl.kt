package com.example.todotasks.data.repository

import com.example.todotasks.data.database.datastore.DataStoreSource
import com.example.todotasks.domain.model.TaskListPreferences
import com.example.todotasks.domain.repository.DataStoreRepository
import com.example.todotasks.ui.model.GroupAndSort
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DataStoreRepositoryImpl @Inject constructor(private val dataStoreSource: DataStoreSource): DataStoreRepository {
    override fun getTaskListPreferences(): Flow<TaskListPreferences> {
        println("getPreferences: ${dataStoreSource.getTaskListPreferences()}")
        return dataStoreSource.getTaskListPreferences()
    }

    override suspend fun setTaskListPreferences(taskListPreferences: TaskListPreferences) {
        dataStoreSource.setTaskListPreferences(taskListPreferences)
    }
}