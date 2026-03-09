package com.example.todotasks.ui.subTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.usecase.DeleteSubtaskUseCase
import com.example.todotasks.domain.usecase.InsertSubTaskUseCase
import com.example.todotasks.domain.usecase.GetAllSubTasksUseCase
import com.example.todotasks.domain.usecase.UpdateSubTaskCompletedUseCase
import com.example.todotasks.domain.usecase.UpdateSubTaskNameUseCase
import com.example.todotasks.ui.task.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTaskViewModel @Inject constructor(
    private val getAllSubTasksUseCase: GetAllSubTasksUseCase,
    private val insertSubTaskUseCase: InsertSubTaskUseCase,
    private val updateSubTaskCompletedUseCase: UpdateSubTaskCompletedUseCase,
    private val deleteSubtaskUseCase: DeleteSubtaskUseCase,
    private val updateSubTaskNameUseCase: UpdateSubTaskNameUseCase
) :
    ViewModel() {
    private val _taskId = MutableStateFlow<Long?>(null)

    val listaSubTasks: StateFlow<List<SubTask>> = _taskId
        .filterNotNull() // solo cuando la id existe
        .flatMapLatest { taskId ->
            getAllSubTasksUseCase(taskId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _subTaskState: MutableSharedFlow<UiState> =
        MutableSharedFlow(replay = 0)
    val subTaskState: SharedFlow<UiState> = _subTaskState

    fun setTaskId(taskId: Long){
        _taskId.value = taskId
    }

    fun addSubTask(idTask: Long, subTaskName: String){
        val subTaskTrimed = subTaskName.trim()
        if (subTaskTrimed.isNotEmpty()) {
            viewModelScope.launch {
                val newSubTask = SubTask(idTask = idTask, title = subTaskName)
                insertSubTaskUseCase(newSubTask)
            }
        }

    }

    fun getCompletedSubTasks(): String {
        val subTasksCompleted = listaSubTasks.value.filter { subTasks -> subTasks.completed }.size
        val allSubTasks = listaSubTasks.value.size

        return "$subTasksCompleted/$allSubTasks"
    }

    fun updateSubTaskCompleted(idSubTask: Long, isChecked: Boolean){
        viewModelScope.launch {
            updateSubTaskCompletedUseCase(idSubTask, isChecked)
        }
    }

    fun updateSubTaskName(idSubTask: Long, subTaskName: String){
        viewModelScope.launch {
            updateSubTaskNameUseCase(idSubTask, subTaskName)
        }
    }

    fun deleteSubtask(id: Long) {
        viewModelScope.launch { deleteSubtaskUseCase(id) }
    }


}