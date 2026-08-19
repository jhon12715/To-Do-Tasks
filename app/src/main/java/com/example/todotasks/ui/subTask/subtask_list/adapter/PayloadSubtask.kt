package com.example.todotasks.ui.subTask.subtask_list.adapter

import com.example.todotasks.ui.model.ScreenMode

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed interface PayloadSubtask {
    data class ScreenModeChanged(val newScreenMode: ScreenMode) : PayloadSubtask
    data class TittleSubTaskChange(val newTittle: String) : PayloadSubtask
    data class IsCompletedSubTaskChange(val newIsCompleted: Boolean) : PayloadSubtask
    data class IsSelectedToDeleteSubTaskChange(val isSelectedToDelete: Boolean) : PayloadSubtask
}


