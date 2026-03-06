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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _listaSubTasks: MutableStateFlow<List<SubTask>> = MutableStateFlow(listOf())
    val listaSubTasks: StateFlow<List<SubTask>> = _listaSubTasks

    fun getSubTasks(idTask: Long) {
        viewModelScope.launch {
            val subTasks = getAllSubTasksUseCase(idTask)
            _listaSubTasks.update { subTasks }
        }

    }

    fun addSubTask(idTask: Long, subTaskName: String): Boolean {
        val subTaskTrimed = subTaskName.trim()
        if (subTaskTrimed.isNotEmpty()) {
            viewModelScope.launch {
                val newSubTask = SubTask(idTask = idTask, title = subTaskName)
                insertSubTaskUseCase(newSubTask)
                _listaSubTasks.update { subTasks -> subTasks + newSubTask }
            }
            return true
        } else {
            return false
        }

    }

    fun getCompletedSubTasks(): String {
        val subTasksCompleted = listaSubTasks.value.filter { subTasks -> subTasks.completed }.size
        val allSubTasks = listaSubTasks.value.size

        return "$subTasksCompleted/$allSubTasks"
    }

    fun updateSubTaskCompleted(idSubTask: Long, isChecked: Boolean): Boolean {
        viewModelScope.launch {
            updateSubTaskCompletedUseCase(idSubTask, isChecked)

            val newList: List<SubTask> = _listaSubTasks.value.map { subTask ->
                if (subTask.id == idSubTask) {
                    subTask.copy(completed = isChecked)
                } else subTask
            }

            _listaSubTasks.update { newList }
        }
        return true
    }

    fun updateSubTaskName(idSubTask: Long, subTaskName: String): Boolean {
        viewModelScope.launch {
            updateSubTaskNameUseCase(idSubTask, subTaskName)

            val newList: List<SubTask> = _listaSubTasks.value.map { subTask ->
                if (subTask.id == idSubTask) {
                    subTask.copy(title = subTaskName)
                } else subTask
            }

            _listaSubTasks.update { newList }

        }
        return true
    }

    fun deleteSubtask(id: Long) {
        viewModelScope.launch { deleteSubtaskUseCase(id) }
        val newList = _listaSubTasks.value.filter { task -> task.id != id }
        _listaSubTasks.update { newList }

    }


}