package com.example.todotasks.ui.common.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemHeaderBinding
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.common.string_resource.StringResource
import com.example.todotasks.ui.model.DateHeaderSorter

/**
 * Project: To Do Tasks
 * Created by: Jhon

 */

class TaskHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemHeaderBinding.bind(view)
    fun render(header: String) {
        val context = binding.root.context
        val headerVerified = if (verifyIfIsDateHeader(header)) {
            context.getString(StringResource.getDateHeaderResource(header))
        } else if (verifyIfIsPriorityHeader(header)) {
            context.getString(StringResource.getPriorityStringResource(TaskPriority.valueOf(header)))
        } else {
            header
        }
        binding.tvGroupBy.text = headerVerified
    }

    private fun verifyIfIsDateHeader(header: String): Boolean =
        DateHeaderSorter.values().find { it.name == header } != null

    private fun verifyIfIsPriorityHeader(header: String): Boolean =
        TaskPriority.values().find { it.name == header } != null
}