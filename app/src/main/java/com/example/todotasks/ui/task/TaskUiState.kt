package com.example.todotasks.ui.task

import com.example.todotasks.domain.model.GroupByTask
import com.example.todotasks.domain.model.SortByTask
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import com.example.todotasks.ui.model.TypeCategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */
data class TaskUiState (
    val totalTasks: Int = 0,
    val categories: List<TypeCategoryUI> = emptyList(),
    val groupBySelected: GroupByTask = GroupByTask.DATE,
    val orderBySelected: SortByTask = SortByTask.TITTLE,
    val selectedFilterCategoryId: Long = -1,
    val tasksSelectedToDelete: List<Long> = emptyList(),
    val screenMode: ScreenMode = ScreenMode.LOADING
){
    val tasksSelectedToDeleteText get() = "Eliminar ${tasksSelectedToDelete.size}/$totalTasks"
}