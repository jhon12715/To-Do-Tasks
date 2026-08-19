package com.example.todotasks.ui.subTask.subtask_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.domain.model.ParentTaskInfo
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.usecase.DeleteSubtaskUseCase
import com.example.todotasks.domain.usecase.UpdateSubTaskCompletedUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.subTask.applySubTaskFilterAndSort
import com.example.todotasks.ui.subTask.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val updateSubTaskCompletedUseCase: UpdateSubTaskCompletedUseCase,
    private val deleteSubtaskUseCase: DeleteSubtaskUseCase
) : ViewModel() {

    private val _subTaskIsCompletedFilter: MutableStateFlow<IsCompletedFilter> =
        MutableStateFlow(IsCompletedFilter.ALL)

    private val _parentTaskInfo: MutableStateFlow<ParentTaskInfo?> =
        MutableStateFlow(null)

    //SubTask
    private val subTaskList: StateFlow<List<SubTask>?> = _parentTaskInfo.filterNotNull()
        .flatMapLatest { repository.getAllSubTasks(it.taskId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val subTaskSelectedToDelete: MutableStateFlow<List<Long>> =
        MutableStateFlow(emptyList())

    private val subTaskUIList: StateFlow<List<SubTaskUI>?> =
        combine(
            subTaskList.filterNotNull(),
            subTaskSelectedToDelete
        ) { subTaskList, subTaskSelectedToDelete ->
            subTaskList.map {
                it.toUI()
                    .copy(
                        isSelectedToDelete = subTaskSelectedToDelete.contains(it.id)
                    )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    //Event Result
    private val _subTaskEventResult: MutableSharedFlow<ResultEvent> =
        MutableSharedFlow(replay = 0)
    val subTaskEventResult: SharedFlow<ResultEvent> = _subTaskEventResult

    private val screenMode: MutableStateFlow<ScreenMode> =
        MutableStateFlow(ScreenMode.LOADING)

    // Ui State
    val subTaskUiState: StateFlow<SubTaskUiState> =
        combine(
            _subTaskIsCompletedFilter,
            subTaskUIList.filterNotNull(),
            screenMode
        ) { filter, subTaskList, screenMode ->
            SubTaskUiState(
                tittle = _parentTaskInfo.value?.taskName ?: "",
                filterSelected = filter,
                listSubtasks = subTaskList.applySubTaskFilterAndSort(filter),
                subTasksCompleted = subTaskList.count { it.isCompleted },
                subTaskTotal = subTaskList.size,
                subTaskSelectedToDelete = subTaskList.count { it.isSelectedToDelete },
                screenMode = screenMode
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SubTaskUiState()
        )

    init {
        finishLoading()
    }

    private fun finishLoading(){
        viewModelScope.launch {
            subTaskUIList.filterNotNull()
                .first()

            screenMode.update {
                ScreenMode.NORMAL
            }
        }
    }

    fun setParentTaskInfo(taskId: Long, taskName: String) {
        _parentTaskInfo.update {
            ParentTaskInfo(taskId, taskName)
        }
    }

    fun onEvent(event: SubTaskUiEvent) {
        when (event) {
            is SubTaskUiEvent.UpdateFilterSubTaskCompleted -> updateFilterSubTaskCompleted(event.filterCompleted)
            is SubTaskUiEvent.UpdateSubTaskCompleted -> updateSubTaskCompleted(
                event.subTaskId,
                event.isCompleted
            )

            is SubTaskUiEvent.AddSubTaskToDeleteList -> addSubTaskToDeleteList(event.subTaskId)
            is SubTaskUiEvent.RemoveSubTaskToDeleteList -> removeSubTaskToDeleteList(event.subTaskId)
            SubTaskUiEvent.CommitSubTaskDelete -> commitSubTaskDelete()
            SubTaskUiEvent.ExitFromDeleteMode -> exitFromDeleteMode()
        }
    }

    private fun updateFilterSubTaskCompleted(filterCompleted: IsCompletedFilter) {
        _subTaskIsCompletedFilter.update { filterCompleted }
    }

    private fun updateSubTaskCompleted(subTaskId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            updateSubTaskCompletedUseCase(subTaskId, isChecked)
        }
    }

    private fun addSubTaskToDeleteList(subTaskId: Long) {
        if (screenMode.value == ScreenMode.NORMAL) screenMode.update { ScreenMode.DELETE }
        subTaskSelectedToDelete.update {
            it + listOf(subTaskId)
        }
    }

    private fun removeSubTaskToDeleteList(subTaskId: Long) {
        subTaskSelectedToDelete.update { list ->
            list.filter { subTaskIdList -> subTaskIdList != subTaskId }
        }
    }

    private fun commitSubTaskDelete() {
        viewModelScope.launch {
            val subTasksToDelete = subTaskSelectedToDelete.value
            _subTaskEventResult.emit(deleteSubtaskUseCase(subTasksToDelete))
            subTaskSelectedToDelete.update { emptyList() }
            screenMode.update { ScreenMode.NORMAL }
        }
    }

    private fun exitFromDeleteMode() {
        screenMode.update { ScreenMode.NORMAL }
        subTaskSelectedToDelete.update { emptyList() }
    }

    fun getTaskId(): Long = _parentTaskInfo.value!!.taskId

}