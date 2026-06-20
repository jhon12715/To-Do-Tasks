package com.example.todotasks.ui.task

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskListItem
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllCategoriesUseCase
import com.example.todotasks.domain.usecase.GetAllTasksUseCase
import com.example.todotasks.domain.usecase.InsertCategoryUseCase
import com.example.todotasks.domain.usecase.UpsertTaskUseCase
import com.example.todotasks.domain.usecase.UpdateTaskCompletedUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.mapper.toDomain
import com.example.todotasks.ui.mapper.toFormUi
import com.example.todotasks.ui.mapper.toUi
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskCompletedUseCase: UpdateTaskCompletedUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val insertCategoryUseCase: InsertCategoryUseCase
) : ViewModel() {
    var start = SystemClock.elapsedRealtime()
    //Result
    private val _resultEvent: MutableSharedFlow<ResultEvent> = MutableSharedFlow(replay = 0)
    val resultEvent: SharedFlow<ResultEvent> = _resultEvent

    //Filter
    private val _selectedFilterCategoryId = MutableStateFlow(-1L)

    //Loading
    private val _isLoading = MutableStateFlow(true)

    private val taskFilter: MutableStateFlow<TaskFilter> =
        MutableStateFlow(TaskFilter.ALL)
    //Tasks

    private val tasks: StateFlow<List<TaskListItem>?> = getAllTasksUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val taskState: StateFlow<List<TaskUI>> = combine(
        tasks,
        _selectedFilterCategoryId,
        taskFilter
    ) { tasks, categoryId, filter ->
        tasks.orEmpty()
            .map { it.toUi() } // -> toUi() SOLO se ejecuta si cambian las tareas reales
            .filterTaskByCategory(categoryId)
            .applyTaskFilterAndSort(filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //Categories

    private val categories: StateFlow<List<Category>?> = getAllCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val categoriesState: Flow<List<TypeCategoryUI>> = combine(
        categories,
        _selectedFilterCategoryId
    ) { categories, categoryId ->
        categories.orEmpty()
            .toUi(categoryId)
    }

    //UIState

    val taskUIState: StateFlow<TaskUiState> =
        combine(
            taskState,
            categoriesState,
            _selectedFilterCategoryId,
            taskFilter,
            _isLoading
        ) { tasks, categories, categoryId, filter, isLoading ->
            println("tiempo ahora 11: ${SystemClock.elapsedRealtime() - start} ms")
            TaskUiState(
                tasks = tasks,
                categories = categories,
                selectedFilterCategoryId = categoryId,
                selectedFilterCompletedTask = filter,
                isLoading = isLoading
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState()
        )

    //Task Form
    private val _taskFormState: MutableStateFlow<TaskFormState> = MutableStateFlow(TaskFormState())
    val taskFormState: StateFlow<TaskFormState> = _taskFormState

    private var originalTask: Task? = null
    fun onEvent(event: TaskUiEvent) {
        when (event) {
            //Activity
            is TaskUiEvent.UpdateTaskFilter -> updateTaskFilter(event.taskFilter)
            is TaskUiEvent.UpdateCategoryTaskSelected -> updateCategorySelected(event.categorySelectedId)
            is TaskUiEvent.UpdateCompletedTask -> updateCompletedTask(
                event.taskId,
                event.isCompleted
            )

            is TaskUiEvent.DeletedTask -> deleteTask(event.taskId)
            //FormDialog
            is TaskUiEvent.UpdateNameTaskForm -> updateNameTaskForm(event.taskForm, event.idForm)
            is TaskUiEvent.UpdateDateTaskForm -> updateDateTaskForm(event.dateForm)
            is TaskUiEvent.UpdateCategoryIdTaskForm -> updateCategoryIdTaskSelectedForm(event.categoryFormSelectedId)
            is TaskUiEvent.UpdatePriorityTaskForm -> updatePriorityTaskForm(event.taskPriorityForm)
            TaskUiEvent.UpsertTaskForm -> upsertTaskForm()
            //CategoryTaskSheet
            is TaskUiEvent.UpsertCategory -> insertCategory(event.name)
            TaskUiEvent.OpenNewTaskDialog -> newTaskForm()
            is TaskUiEvent.OpenEditTaskDialog -> editTaskForm(
                event.taskId,
                event.taskName,
                event.taskPriority,
                event.taskDate,
                event.categoryId
            )
        }
    }

    init {
        viewModelScope.launch {
            combine(tasks, categories) { t, c ->
                t != null && c != null
            }.first {it}

            _isLoading.value = false
        }
    }

    //Actions

    private fun updateCompletedTask(id: Long, isCompleted: Boolean) {

        viewModelScope.launch {
            updateTaskCompletedUseCase(id, isCompleted)
        }

    }

    private fun deleteTask(id: Long) {
        viewModelScope.launch {

            _resultEvent.emit(deleteTaskUseCase(id))

        }

    }


    private fun updateTaskFilter(newFilter: TaskFilter) {
        taskFilter.value = newFilter
    }

    private fun updateCategorySelected(idCategorySelected: Long) {
        _selectedFilterCategoryId.value = idCategorySelected
    }

    // CategoryTaskSheet
    private fun insertCategory(category: String) {
        viewModelScope.launch {
            _resultEvent.emit(insertCategoryUseCase(category))
        }
    }

    //FormDialog Events
    private fun updateNameTaskForm(taskForm: String, id: Long) {

        val taskNameTrimed = taskForm.trim()
        println("tasks: ${tasks.value}")
        val exists = tasks.value!!.any {
            it.task.task.equals(taskNameTrimed.trim(), ignoreCase = true) &&
                    it.task.id != id
        } || taskNameTrimed.isEmpty()

        _taskFormState.update { it.copy(taskName = taskNameTrimed, isValid = !exists) }

    }

    private fun updateDateTaskForm(newDateForm: LocalDate?) {
        _taskFormState.update { it.copy(date = newDateForm) }
    }

    private fun updateCategoryIdTaskSelectedForm(categoryFormSelectedId: Long?) {
        _taskFormState.update { it.copy(categoryId = categoryFormSelectedId) }
    }

    private fun updatePriorityTaskForm(taskPriorityForm: TaskPriority) {
        _taskFormState.update { it.copy(priority = taskPriorityForm) }
    }

    private fun newTaskForm() {
        originalTask = null
        _taskFormState.update {
            it.copy(
                id = 0,
                taskName = "",
                priority = TaskPriority.NORMAL,
                date = null,
                categoryId = _selectedFilterCategoryId.value,
                isValid = false,
                availableCategories = categories.value!!.toFormUi()
            )
        }
    }

    private fun editTaskForm(
        id: Long,
        taskName: String,
        priority: TaskPriority,
        date: LocalDate?,
        categoryId: Long?
    ) {
        originalTask = taskState.value.first { it.id == id }.toDomain()
        _taskFormState.update {
            it.copy(
                id = id,
                taskName = taskName,
                priority = priority,
                date = date,
                categoryId = categoryId,
                isValid = true,
                availableCategories = categories.value!!.toFormUi()
            )
        }
    }

    private fun upsertTaskForm() {

        viewModelScope.launch {
            val taskForm = _taskFormState.value
            println("taskForm: $taskForm")
            val newTask = originalTask?.copy(
                task = taskForm.taskName,
                priority = taskForm.priority,
                date = taskForm.date,
                categoryId = if (taskForm.categoryId == -1L) null else taskForm.categoryId
            ) ?: Task(
                task = taskForm.taskName,
                priority = taskForm.priority,
                date = taskForm.date,
                categoryId = if (taskForm.categoryId == -1L) null else taskForm.categoryId
            )
            _resultEvent.emit(upsertTaskUseCase(newTask))
        }

    }

}