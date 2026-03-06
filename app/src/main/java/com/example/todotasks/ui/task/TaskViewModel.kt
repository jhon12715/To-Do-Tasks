package com.example.todotasks.ui.task

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllSubTasksUseCase
import com.example.todotasks.domain.usecase.GetAllTasksUseCase
import com.example.todotasks.domain.usecase.InsertTaskUseCase
import com.example.todotasks.domain.usecase.UpdateTaskUseCase
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
    private val getAllSubTasksUseCase: GetAllSubTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _listaTasks = getAllTasksUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val listaTasks: StateFlow<List<Task>> = _listaTasks

    private val _taskState: MutableSharedFlow<UiState> =
        MutableSharedFlow<UiState>(replay = 0)
    val taskState: SharedFlow<UiState> = _taskState

    fun insertTask(taskName: String) {
        val newTask = Task(task = taskName)

        viewModelScope.launch {
            try {
                _taskState.emit(UiState.Loading)
                val id = insertTaskUseCase(newTask)
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

                val list = _listaTasks.value

                val newList: List<Task> = list.map { task ->
                    if (task.id == taskToUpdate.id) {
                        task.copy(task = taskToUpdate.task)
                    } else task
                }

                _taskState.emit(UiState.Success("La tarea se ha actualizado correctamente"))
            } catch (e: SQLiteConstraintException) {
                _taskState.emit(UiState.Error("Esta tarea ya existe"))
            }
        }

    }

    fun getAllTasks() {

        viewModelScope.launch {
            getAllTasksUseCase()
        }
    }

    fun getAllSubTasks() {


    }

    fun editTask() {

    }

    suspend fun getSubTasksCompleted(id: Long): String {

        val allSubTasks = getAllSubTasksUseCase(id)
        val subTasksCompleted = allSubTasks.size

        return "$subTasksCompleted/$allSubTasks"
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            _taskState.emit(UiState.Loading)
            deleteTaskUseCase(id)
            val newList = _listaTasks.value.filter { task -> task.id != id }
            _taskState.emit(UiState.Success("La tarea se ha borrado correctamente"))
        }

    }
}