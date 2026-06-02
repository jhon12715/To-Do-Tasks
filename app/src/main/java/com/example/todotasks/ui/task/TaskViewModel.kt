package com.example.todotasks.ui.task

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllTasksUseCase
import com.example.todotasks.domain.usecase.InsertTaskUseCase
import com.example.todotasks.domain.usecase.UpdateTaskCompletedUseCase
import com.example.todotasks.domain.usecase.UpdateTaskUseCase
import com.example.todotasks.ui.task.model.TaskUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val updateTaskCompletedUseCase: UpdateTaskCompletedUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val taskFilter: MutableStateFlow<TaskFilter> =
        MutableStateFlow(TaskFilter.ALL)

    private val allTasks =
        taskFilter
            .flatMapLatest { filter -> getAllTasksUseCase() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    val taskUiState: StateFlow<List<TaskUI>> = combine(allTasks, taskFilter) { list, filter ->
        list.applyFilterAndSort(filter)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _taskState: MutableSharedFlow<UiState> =
        MutableSharedFlow(replay = 0)
    val taskState: SharedFlow<UiState> = _taskState

    private val _taskPriority = MutableStateFlow(TaskPriority.NORMAL)
    val taskPriority: StateFlow<TaskPriority> = _taskPriority

    private val _date = MutableStateFlow<LocalDate?>(null)
    val date: StateFlow<LocalDate?> = _date

    fun insertTask(taskName: String, priority: TaskPriority, date: LocalDate?) {

        viewModelScope.launch {
            _taskState.emit(UiState.Loading)
            _taskState.emit(insertTaskUseCase(taskName, priority, date))
        }

    }

    fun updateTask(id: Long, newTaskName: String, oldTaskName: String, newPriority: TaskPriority, oldPriority: TaskPriority, newDate: LocalDate?, oldDate: LocalDate?) {

        viewModelScope.launch {
                _taskState.emit(UiState.Loading)
                _taskState.emit(updateTaskUseCase(id, newTaskName, oldTaskName, newPriority, oldPriority, newDate, oldDate))
        }

    }

    fun updateCompletedTask(id: Long, isCompleted: Boolean) {

        viewModelScope.launch {
                updateTaskCompletedUseCase(id, isCompleted)
        }

    }

    fun deleteTask(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _taskState.emit(UiState.Loading)
            deleteTaskUseCase(id)
            _taskState.emit(UiState.Success("La tarea se ha borrado correctamente"))
        }

    }

    fun setDate(date: LocalDate?){
        _date.value = date
    }

    fun setTaskFilter(newFilter: TaskFilter) {
        taskFilter.value = newFilter
    }

    fun updateTaskPriority(priority: TaskPriority) {
        println("actualizada: $priority")
        _taskPriority.value = priority
    }
}