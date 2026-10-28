package com.example.todotasks.ui.common.string_resource

import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.model.result_event.AppResultEvent

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

interface StringProvider{
    fun getStringResource(stringResource: Int): String
    fun getDateHeaderResource(header: String): String
    fun getResultEventResource(event: AppResultEvent): String
    fun getPriorityStringResource(priority: TaskPriority): String
}