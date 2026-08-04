package com.example.todotasks.ui.task.task_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.domain.model.TaskIsCompletedFilter
import com.example.todotasks.domain.model.TaskListPreferences
import com.example.todotasks.domain.repository.DataStoreRepository
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.usecase.DeleteCategoryUseCase
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllCategoriesUseCase
import com.example.todotasks.domain.usecase.UpdateTaskCompletedUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.mapper.toDomain
import com.example.todotasks.ui.mapper.toUi
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.model.TypeTaskListItem
import com.example.todotasks.ui.core.extensions.applyTaskGroupByAndSorterBy
import com.example.todotasks.ui.core.extensions.filterByIsCompleted
import com.example.todotasks.ui.task.group_and_sort.GroupAndSortState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dataStoreRepository: DataStoreRepository,
    getAllCategoriesUseCase: GetAllCategoriesUseCase,
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

    //Categories

    val categories: StateFlow<List<Category>?> = getAllCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val categoriesState: StateFlow<List<TypeCategoryUI>?> = combine(
        categories.filterNotNull(),
        _selectedFilterCategoryId
    ) { categories, categoryId ->
        categories
            .toUi(categoryId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    //GroupAndOrderState
    val taskListPreferences: StateFlow<TaskListPreferences?> =
        dataStoreRepository.getTaskListPreferences()
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private var currentListPreferences: TaskListPreferences? = null

    //UIState

    val taskUIState: StateFlow<TaskUiState> =
        combine(
            categoriesState.filterNotNull(),
            _selectedFilterCategoryId,
            _isLoading
        ) { categories, categoryId, isLoading ->
            TaskUiState(
                categories = categories,
                selectedFilterCategoryId = categoryId,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState()
        )

    fun onEvent(event: TaskUiEvent) {
        when (event) {
            is TaskUiEvent.UpdateTaskFilter -> updateTaskFilter(event.taskIsCompletedFilter)
            is TaskUiEvent.UpdateCategoryTaskSelected -> updateCategorySelected(event.categorySelectedId)
            is TaskUiEvent.UpdateCompletedTask -> updateCompletedTask(
                event.taskId,
                event.isCompleted
            )

            is TaskUiEvent.DeleteTask -> deleteTask(event.taskId)
            is TaskUiEvent.DeleteCategory -> deleteCategory(event.category)
            is TaskUiEvent.UpdateGroupBy -> updateGroupBy(event.groupBy)
            is TaskUiEvent.UpdateSortBy -> updateOrderBy(event.sortBy)
        }
    }

    private fun updateTaskFilter(newFilter: TaskIsCompletedFilter) {
        val newTaskListPreferences = taskListPreferences.value!!.copy(
            isCompletedFilter = newFilter
        )
        updateTaskListPreferencesDataStore(newTaskListPreferences)
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


    fun getTasksByCategory(categoryId: Long): Flow<List<TypeTaskListItem>> {
        val start = System.currentTimeMillis()
        val taskByCategory =
            when (categoryId) {
                -1L -> taskRepository.getAllTasks()
                else -> taskRepository.getAllTasksByCategory(categoryId)
            }
        return combine(
            taskByCategory.onEach { Log.d("TIME", "Category: ${System.currentTimeMillis() - start} ms") },
            taskListPreferences.filterNotNull().onEach { Log.d("TIME", "Preferences: ${System.currentTimeMillis() - start} ms") }
        ) { tasks, taskListPreferences ->
            println(Log.d("TIME", "Entra: ${System.currentTimeMillis() - start} ms"))
            val v = tasks
                .map { it.toUi() }
                .filterByIsCompleted(taskListPreferences.isCompletedFilter)
                .applyTaskGroupByAndSorterBy(
                    taskListPreferences.groupAndSort.groupBy,
                    taskListPreferences.groupAndSort.sortBy
                )
            return@combine v
        }
    }

    private fun updateGroupBy(groupBy: GroupByTask) {
        val taskListPreferences = taskListPreferences.value
        val newTaskListPreferences = taskListPreferences!!.copy(
            groupAndSort = taskListPreferences.groupAndSort.copy(groupBy = groupBy))
                    updateTaskListPreferencesDataStore (newTaskListPreferences)
    }

    private fun updateOrderBy(sortBy: SortByTask) {
        val taskListPreferences = taskListPreferences.value
        val newTaskListPreferences = taskListPreferences!! .copy(
                groupAndSort = taskListPreferences.groupAndSort.copy(sortBy = sortBy))
                        updateTaskListPreferencesDataStore (newTaskListPreferences)
    }

    private fun updateTaskListPreferencesDataStore(newTaskListPreferences: TaskListPreferences) {
        viewModelScope.launch { dataStoreRepository.setTaskListPreferences(newTaskListPreferences) }
    }

}