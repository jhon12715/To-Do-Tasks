package com.example.todotasks.ui.task

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllSubTasksUseCase
import com.example.todotasks.domain.usecase.GetAllTasksUseCase
import com.example.todotasks.domain.usecase.InsertTaskUseCase
import com.example.todotasks.domain.usecase.UpdateTaskCompletedUseCase
import com.example.todotasks.domain.usecase.UpdateTaskUseCase
import com.example.todotasks.ui.task.model.TaskUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val UpdateTaskCompletedUseCase: UpdateTaskCompletedUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _listaTasks = getAllTasksUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val listaTasks: StateFlow<List<TaskUI>> = _listaTasks

    private val _taskState: MutableSharedFlow<UiState> =
        MutableSharedFlow(replay = 0)
    val taskState: SharedFlow<UiState> = _taskState

    fun insertTask(taskName: String) {
        val newTask = Task(task = taskName)

        viewModelScope.launch {
            try {
                _taskState.emit(UiState.Loading)
                insertTaskUseCase(newTask)
                _taskState.emit(UiState.Success("La tarea se ha insertado correctamente"))
            } catch (e: SQLiteConstraintException) {
                _taskState.emit(UiState.Error("Esta tarea ya existe"))
            }
        }

    }

    fun updateTask(id: Long, taskName: String) {

        val taskToUpdate = Task(id, taskName)

        viewModelScope.launch {
            try {
                _taskState.emit(UiState.Loading)
                updateTaskUseCase(taskToUpdate)

                _taskState.emit(UiState.Success("La tarea se ha actualizado correctamente"))
            } catch (e: SQLiteConstraintException) {
                _taskState.emit(UiState.Error("Esta tarea ya existe"))
            }
        }

    }

    fun updateCompletedTask(id: Long, isCompleted: Boolean) {

        viewModelScope.launch {
            try {
                _taskState.emit(UiState.Loading)
                UpdateTaskCompletedUseCase(id, isCompleted)

                _taskState.emit(UiState.Success("La tarea se ha actualizado correctamente"))
            } catch (e: SQLiteConstraintException) {
                _taskState.emit(UiState.Error("Esta tarea ya existe"))
            }
        }

    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            _taskState.emit(UiState.Loading)
            deleteTaskUseCase(id)
            _taskState.emit(UiState.Success("La tarea se ha borrado correctamente"))
        }

    }
}