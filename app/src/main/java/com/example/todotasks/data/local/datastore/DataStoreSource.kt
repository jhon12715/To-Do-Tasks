package com.example.todotasks.data.local.datastore

import androidx.datastore.core.DataStore
import com.example.todotasks.domain.model.GroupAndSort
import com.example.todotasks.domain.model.SettingsApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DataStoreSource @Inject constructor(private val dataStore: DataStore<SettingsApp>) {

    fun getTaskGroupAndSort(): Flow<GroupAndSort> =
        dataStore.data.map { settings -> settings.groupAndSort }

    suspend fun setTaskGroupAndSort(groupAndSort: GroupAndSort) {
        dataStore.updateData { settings ->
            settings.copy(
                groupAndSort = groupAndSort
            )
        }
    }

}