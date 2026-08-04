package com.example.todotasks.ui.model

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class TypeTaskListItem(){
    data class TaskItem(val task: TaskUI): TypeTaskListItem()
    data class HeaderItem(val header: String): TypeTaskListItem()
}