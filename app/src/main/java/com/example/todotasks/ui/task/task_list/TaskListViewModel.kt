package com.example.todotasks.ui.task.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.R
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.model.GroupAndSortTask
import com.example.todotasks.domain.repository.DataStoreRepository
import com.example.todotasks.domain.repository.TaskRepository
import com.example.todotasks.domain.usecase.DeleteCategoryUseCase
import com.example.todotasks.domain.usecase.DeleteTaskUseCase
import com.example.todotasks.domain.usecase.GetAllCategoriesUseCase
import com.example.todotasks.domain.usecase.UpdateTaskCompletedUseCase
import com.example.todotasks.ui.mapper.toDomain
import com.example.todotasks.ui.mapper.toUi
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.model.TypeTaskListItem
import com.example.todotasks.ui.task.applyTaskGroupByAndSorterBy
import com.example.todotasks.ui.task.filterByIsCompleted
import com.example.todotasks.domain.model.IsCompletedFilter
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.result_event.AppResultEvent
import com.example.todotasks.ui.common.string_resource.StringProvider
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.task.TaskUiEvent
import com.example.todotasks.ui.task.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
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
    dataStoreRepository: DataStoreRepository,
    getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val updateTaskCompletedUseCase: UpdateTaskCompletedUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {

    //Result
    private val _resultEvent: MutableSharedFlow<AppResultEvent> = MutableSharedFlow(replay = 0)
    val resultEvent: SharedFlow<AppResultEvent> = _resultEvent

    //Filter
    private val _selectedFilterCategoryId = MutableStateFlow(-1L)
    private val _isCompletedFilter = MutableStateFlow(IsCompletedFilter.ALL)

    //Categories

    val categories: StateFlow<List<Category>?> = getAllCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val categoriesUI: StateFlow<List<TypeCategoryUI>?> = combine(
        categories.filterNotNull(),
        _selectedFilterCategoryId
    ) { categories, categoryId ->
        categories.toUi(categoryId, stringProvider.getStringResource(R.string.category_all))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    //GroupAndOrderState
    val groupAndSort: StateFlow<GroupAndSortTask?> =
        dataStoreRepository.getTaskGroupAndSort()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    private val _screenMode: MutableStateFlow<ScreenMode> = MutableStateFlow(ScreenMode.NORMAL)
    private val _totalTasks: Flow<Int> = taskRepository.getAllTasks().map { it.size }
    private val _tasksSelectedToDelete: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())

    //UIState

    val taskUIState: StateFlow<TaskUiState> =
        combine(
            categoriesUI.filterNotNull(),
            _selectedFilterCategoryId,
            _screenMode,
            _totalTasks,
            _tasksSelectedToDelete
        ) { categories, categoryId, screenMode, totalTasks, tasksSelectedToDelete ->
            TaskUiState(
                categories = categories,
                selectedFilterCategoryId = categoryId,
                screenMode = screenMode,
                totalTasks = totalTasks,
                tasksSelectedToDelete = tasksSelectedToDelete.map { task -> task.id }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState()
        )

    fun onEvent(event: TaskUiEvent) {
        when (event) {
            is TaskUiEvent.UpdateTaskIsCompletedFilter -> updateTaskIsCompletedFilter(event.taskIsCompletedFilter)
            is TaskUiEvent.UpdateCategoryTaskSelected -> updateCategorySelected(event.categorySelectedId)
            is TaskUiEvent.UpdateCompletedTask -> updateCompletedTask(
                event.taskId,
                event.isCompleted
            )

            is TaskUiEvent.DeleteCategory -> deleteCategory(event.category)
            is TaskUiEvent.UpdateScreenMode -> updateScreenMode(event.screenMode)
            is TaskUiEvent.AddTaskToDelete -> addTaskToDelete(event.task)
            is TaskUiEvent.RemoveTaskToDelete -> removeTaskToDelete(event.task)
            TaskUiEvent.CommitTaskToDelete -> commitTaskToDelete()
        }
    }

    private fun updateTaskIsCompletedFilter(newFilter: IsCompletedFilter) {
        _isCompletedFilter.update { newFilter }
    }

    private fun updateCategorySelected(idCategorySelected: Long) {
        _selectedFilterCategoryId.value = idCategorySelected
    }

    private fun updateCompletedTask(id: Long, isCompleted: Boolean) {

        viewModelScope.launch {
            updateTaskCompletedUseCase(id, isCompleted)
        }

    }

    private fun deleteCategory(category: CategoryUI) {
        viewModelScope.launch {
            _resultEvent.emit(deleteCategoryUseCase(category.toDomain()))
        }
    }


    fun getTasksByCategory(categoryId: Long): Flow<List<TypeTaskListItem>> {
        val taskByCategory =
            when (categoryId) {
                -1L -> taskRepository.getAllTasks()
                else -> taskRepository.getAllTasksByCategory(categoryId)
            }
        return combine(
            taskByCategory,
            _isCompletedFilter,
            groupAndSort.filterNotNull(),
            _tasksSelectedToDelete,
        ) { tasks, isCompletedFilter, groupAndSort, tasksSelectedToDelete ->

            tasks.map {
                it.toUi().copy(
                    isSelectedToDelete = tasksSelectedToDelete.contains(it.task)
                )
            }
                .filterByIsCompleted(isCompletedFilter)
                .applyTaskGroupByAndSorterBy(
                    groupAndSort.groupBy,
                    groupAndSort.sortBy,
                    groupAndSort.sortOrder
                )
        }
    }

    private fun updateScreenMode(newScreenMode: ScreenMode) {
        _screenMode.update { newScreenMode }
        if (newScreenMode != ScreenMode.DELETE) _tasksSelectedToDelete.update { emptyList() }
    }

    private fun addTaskToDelete(task: TaskUI) {
        _tasksSelectedToDelete.update {
            it + task.toDomain()
        }
    }

    private fun removeTaskToDelete(task: TaskUI) {
        _tasksSelectedToDelete.update { list ->
            list.filter { item -> item.id != task.id }
        }
    }

    private fun commitTaskToDelete() {
        viewModelScope.launch {

            _resultEvent.emit(deleteTaskUseCase(_tasksSelectedToDelete.value))
            _tasksSelectedToDelete.update { emptyList() }
            _screenMode.update { ScreenMode.NORMAL }

        }
    }

    fun verifyCurrentCategoryIdExists(): Long? {
        val currentCategoryId = taskUIState.value.selectedFilterCategoryId
        return if(currentCategoryId < 1) null else currentCategoryId
    }

}