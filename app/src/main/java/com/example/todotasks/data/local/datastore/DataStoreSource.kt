package com.example.todotasks.data.local.datastore

import androidx.datastore.core.DataStore
import com.example.todotasks.domain.model.GroupAndSortSubTask
import com.example.todotasks.domain.model.GroupAndSortTask
import com.example.todotasks.domain.model.SettingsApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DataStoreSource @Inject constructor(private val dataStore: DataStore<SettingsApp>) {

    fun getTaskGroupAndSortTask(): Flow<GroupAndSortTask> =
        dataStore.data.map { settings -> settings.groupAndSortTask }

    suspend fun setTaskGroupAndSortTask(groupAndSort: GroupAndSortTask) {
        dataStore.updateData { settings ->
            settings.copy(
                groupAndSortTask = groupAndSort
            )
        }
    }

    fun getTaskGroupAndSortSubTask(): Flow<GroupAndSortSubTask> =
        dataStore.data.map { settings -> settings.groupAndSortSubTask }

    suspend fun setTaskGroupAndSortSubTask(groupAndSort: GroupAndSortSubTask) {
        dataStore.updateData { settings ->
            settings.copy(
                groupAndSortSubTask = groupAndSort
            )
        }
    }

}