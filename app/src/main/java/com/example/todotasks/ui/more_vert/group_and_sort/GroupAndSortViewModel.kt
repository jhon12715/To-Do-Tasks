package com.example.todotasks.ui.more_vert.group_and_sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.GroupAndSortSubTask
import com.example.todotasks.domain.model.GroupAndSortTask
import com.example.todotasks.domain.model.GroupBySubTask
import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortBySubTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.SortOrder
import com.example.todotasks.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@HiltViewModel
class GroupAndSortViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    //GroupAndOrderState
    val groupAndSortTask: StateFlow<GroupAndSortTask?> =
        dataStoreRepository.getTaskGroupAndSort()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val groupAndSortSubTask: StateFlow<GroupAndSortSubTask?> =
        dataStoreRepository.getSubTaskGroupAndSort()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    fun onEvent(event: GroupAndSortEvent) {
        when (event) {
            is GroupAndSortEvent.UpdateGroupByTask -> updateGroupByTask(event.groupBy)
            is GroupAndSortEvent.UpdateSortByTask -> updateSortByTask(event.sortBy)
            GroupAndSortEvent.UpdateSortOrderTask -> updateSortOrderTask()
            is GroupAndSortEvent.UpdateGroupBySubTask -> updateGroupBySubTask(event.groupBy)
            is GroupAndSortEvent.UpdateSortBySubTask -> updateSortBySubTask(event.sortBy)
            GroupAndSortEvent.UpdateSortOrderSubTask -> updateSortOrderSubTask()
        }
    }

    private fun updateGroupByTask(newGroupBy: GroupByTask) {
        val newGroupAndSort = groupAndSortTask.value!!.copy(
            groupBy = newGroupBy
        )
        updateGroupAndSortTaskDataStore(newGroupAndSort)
    }

    private fun updateSortByTask(newSortBy: SortByTask) {
        val newGroupAndSort = groupAndSortTask.value!!.copy(
            sortBy = newSortBy
        )
        updateGroupAndSortTaskDataStore(newGroupAndSort)
    }

    private fun updateSortOrderTask() {
        val currentSortOrder = groupAndSortTask.value!!.sortOrder
        val newSortOrder = if (currentSortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC

        val newGroupAndSort = groupAndSortTask.value!!.copy(
            sortOrder = newSortOrder
        )

        updateGroupAndSortTaskDataStore(newGroupAndSort)
    }

    private fun updateGroupAndSortTaskDataStore(newGroupAndSort: GroupAndSortTask) {
        viewModelScope.launch { dataStoreRepository.setTaskGroupAndSort(newGroupAndSort) }
    }

    private fun updateGroupBySubTask(newGroupBy: GroupBySubTask) {
        val newGroupAndSort = groupAndSortSubTask.value!!.copy(
            groupBy = newGroupBy
        )
        updateGroupAndSortSubTaskDataStore(newGroupAndSort)
    }

    private fun updateSortBySubTask(newSortBy: SortBySubTask) {
        val newGroupAndSort = groupAndSortSubTask.value!!.copy(
            sortBy = newSortBy
        )
        updateGroupAndSortSubTaskDataStore(newGroupAndSort)
    }

    private fun updateSortOrderSubTask() {
        val currentSortOrder = groupAndSortSubTask.value!!.sortOrder
        val newSortOrder = if (currentSortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC

        val newGroupAndSort = groupAndSortSubTask.value!!.copy(
            sortOrder = newSortOrder
        )

        updateGroupAndSortSubTaskDataStore(newGroupAndSort)

    }

    private fun updateGroupAndSortSubTaskDataStore(newGroupAndSort: GroupAndSortSubTask) {
        viewModelScope.launch { dataStoreRepository.setSubTaskGroupAndSort(newGroupAndSort) }
    }
}