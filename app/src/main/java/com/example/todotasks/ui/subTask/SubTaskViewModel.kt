package com.example.todotasks.ui.subTask

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.usecase.DeleteSubtaskUseCase
import com.example.todotasks.domain.usecase.InsertSubTaskUseCase
import com.example.todotasks.domain.usecase.GetAllSubTasksUseCase
import com.example.todotasks.domain.usecase.UpdateSubTaskCompletedUseCase
import com.example.todotasks.domain.usecase.UpdateSubTaskUseCase
import com.example.todotasks.ui.task.UiState
import com.example.todotasks.ui.task.model.SubTaskUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTaskViewModel @Inject constructor(
    private val getAllSubTasksUseCase: GetAllSubTasksUseCase,
    private val insertSubTaskUseCase: InsertSubTaskUseCase,
    private val updateSubTaskCompletedUseCase: UpdateSubTaskCompletedUseCase,
    private val deleteSubtaskUseCase: DeleteSubtaskUseCase,
    private val updateSubTaskUseCase: UpdateSubTaskUseCase
) :
    ViewModel() {
    private val _taskId = MutableStateFlow<Long?>(null)
    private val _taskFilter: MutableStateFlow<TaskFilter> =
        MutableStateFlow(TaskFilter.ALL)

    private val allSubTasks: StateFlow<List<SubTask>> = _taskId
        .filterNotNull()
        .flatMapLatest { taskId -> getAllSubTasksUseCase(taskId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val listaSubTasks: StateFlow<SubTaskUi> = combine(
        allSubTasks, // solo cuando la id existe
        _taskFilter
    ) { list, filter ->
        val filteredList = list.applyFilterAndSort(filter)
        val total = list.size
        val completed = list.count { it.completed }
        SubTaskUi(filteredList, total, completed)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SubTaskUi()
    )

    private val _subTaskState: MutableSharedFlow<UiState> =
        MutableSharedFlow(replay = 0)
    val subTaskState: SharedFlow<UiState> = _subTaskState

    private val _priority: MutableStateFlow<TaskPriority> =
        MutableStateFlow(TaskPriority.NORMAL)
    val priority: StateFlow<TaskPriority> = _priority

    fun setTaskId(taskId: Long) {
        _taskId.value = taskId
    }

    fun insertSubTask(idTask: Long, newName: String, newPriority: TaskPriority) {
        viewModelScope.launch {
            _subTaskState.emit(UiState.Loading)
            _subTaskState.emit(insertSubTaskUseCase(idTask, newName, newPriority)
            )
        }

    }

    fun updateSubTaskCompleted(idSubTask: Long, isChecked: Boolean) {
        viewModelScope.launch {
            updateSubTaskCompletedUseCase(idSubTask, isChecked)
        }
    }

    fun updateSubTask(
        idSubTask: Long,
        newName: String,
        oldName: String,
        newPriority: TaskPriority,
        oldPriority: TaskPriority
    ) {
        viewModelScope.launch {
            _subTaskState.emit(UiState.Loading)
            _subTaskState.emit(
                updateSubTaskUseCase(
                    idSubTask,
                    newName,
                    oldName,
                    newPriority,
                    oldPriority
                )
            )
        }
    }

    fun deleteSubtask(id: Long) {
        viewModelScope.launch {
            deleteSubtaskUseCase(id)
        }
    }

    fun updateSubtaskPriority(newPriority: TaskPriority) {
        _priority.value = newPriority
    }

    fun setTaskSubFilter(newFilter: TaskFilter) {
        _taskFilter.value = newFilter
    }

}