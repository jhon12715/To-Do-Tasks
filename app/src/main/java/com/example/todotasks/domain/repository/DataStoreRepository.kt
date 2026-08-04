package com.example.todotasks.domain.repository

import com.example.todotasks.domain.model.TaskListPreferences
import com.example.todotasks.ui.model.GroupAndSort
import kotlinx.coroutines.flow.Flow

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

interface DataStoreRepository{
    fun getTaskListPreferences(): Flow<TaskListPreferences>
    suspend fun setTaskListPreferences(taskListPreferences: TaskListPreferences)
}