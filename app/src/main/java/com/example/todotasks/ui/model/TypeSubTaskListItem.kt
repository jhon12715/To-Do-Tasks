package com.example.todotasks.ui.model

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class TypeSubTaskListItem(){
    data class SubTaskItem(val subTaskUI: SubTaskUI): TypeSubTaskListItem()
    data class HeaderItem(val header: String): TypeSubTaskListItem()
}