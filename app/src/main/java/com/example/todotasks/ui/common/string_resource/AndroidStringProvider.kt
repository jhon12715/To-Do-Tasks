package com.example.todotasks.ui.common.string_resource

import android.content.Context
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.domain.model.result_event.AppResultEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class AndroidStringProvider @Inject constructor(@ApplicationContext private val context: Context) :
    StringProvider {
    override fun getStringResource(stringResource: Int): String = context.getString(stringResource)

    override fun getDateHeaderResource(header: String): String {

        val dateRes = StringResource.getDateHeaderResource(header)

        return context.getString(dateRes)
    }

    override fun getResultEventResource(event: AppResultEvent): String {
        val resultEventRes = StringResource.getResultEventStringResource(event)

        return context.getString(resultEventRes)
    }

    override fun getPriorityStringResource(priority: TaskPriority): String {
        val priorityRes = StringResource.getPriorityStringResource(priority)

        return context.getString(priorityRes)
    }
}