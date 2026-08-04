package com.example.todotasks.data.local.datastore

import androidx.datastore.core.DataStore
import com.example.todotasks.domain.model.SettingsApp
import com.example.todotasks.domain.model.TaskListPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class DataStoreSource @Inject constructor(private val dataStore: DataStore<SettingsApp>) {

    fun getTaskListPreferences(): Flow<TaskListPreferences> =
        dataStore.data.map { settings -> settings.taskListPreferences }

    suspend fun setTaskListPreferences(taskListPreferences: TaskListPreferences) {
        dataStore.updateData { settings ->
            settings.copy(
                taskListPreferences
            )
        }
    }

}