package com.example.todotasks.ui.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.TaskFilter
import com.example.todotasks.domain.model.TaskListItem
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.usecase.DeleteCategoryUseCase
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllCategoriesUseCase
import com.example.todotasks.domain.usecase.GetAllTasksUseCase
import com.example.todotasks.domain.usecase.UpdateTaskCompletedUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.mapper.toDomain
import com.example.todotasks.ui.mapper.toFormUi
import com.example.todotasks.ui.mapper.toUI
import com.example.todotasks.ui.mapper.toUi
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.task.applyTaskFilterAndSort
import com.example.todotasks.ui.task.category_form.CategoryFormState
import com.example.todotasks.ui.task.filterTaskByCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@HiltViewModel
class TaskListViewModel @Inject constructor(
    getAllCategoriesUseCase: GetAllCategoriesUseCase,
    getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskCompletedUseCase: UpdateTaskCompletedUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

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

    //Categories

    val categories: StateFlow<List<Category>?> = getAllCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val categoriesState: StateFlow<List<TypeCategoryUI>> = combine(
        categories,
        _selectedFilterCategoryId
    ) { categories, categoryId ->
        categories.orEmpty()
            .toUi(categoryId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //UIState

    val taskUIState: StateFlow<TaskUiState> =
        combine(
            categoriesState,
            _selectedFilterCategoryId,
            taskFilter,
            _isLoading
        ) { categories, categoryId, filter, isLoading ->
            TaskUiState(
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

    init {
        viewModelScope.launch {
            combine(tasks, categories) { t, c ->
                t != null && c != null
            }.first { it }

            _isLoading.value = false
        }
    }

    fun onEvent(event: TaskUiEvent) {
        when (event) {
            is TaskUiEvent.UpdateTaskFilter -> updateTaskFilter(event.taskFilter)
            is TaskUiEvent.UpdateCategoryTaskSelected -> updateCategorySelected(event.categorySelectedId)
            is TaskUiEvent.UpdateCompletedTask -> updateCompletedTask(
                event.taskId,
                event.isCompleted
            )

            is TaskUiEvent.DeleteTask -> deleteTask(event.taskId)
            is TaskUiEvent.DeleteCategory -> deleteCategory(event.category)
        }
    }

    private fun updateTaskFilter(newFilter: TaskFilter) {
        taskFilter.value = newFilter
    }

    private fun updateCategorySelected(idCategorySelected: Long) {
        _selectedFilterCategoryId.value = idCategorySelected
    }

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

    private fun deleteCategory(category: CategoryUI) {
        viewModelScope.launch {
            _resultEvent.emit(deleteCategoryUseCase(category.toDomain()))
        }
    }


    fun getTasksByCategory(categoryId: Long): Flow<List<TaskUI>> = combine(
        tasks,
        taskFilter
    ) { tasks, filter ->
        tasks.orEmpty()
            .map { it.toUi() }
            .filterTaskByCategory(categoryId)
            .applyTaskFilterAndSort(filter)
    }.flowOn(Dispatchers.Default)

}