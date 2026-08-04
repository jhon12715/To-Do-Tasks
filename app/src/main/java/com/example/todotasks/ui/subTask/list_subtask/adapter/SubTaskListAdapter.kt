package com.example.todotasks.ui.subTask.list_subtask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.ui.core.adapter.TaskHeaderViewHolder
import com.example.todotasks.ui.model.TypeSubTaskListItem
import com.example.todotasks.ui.subTask.SubtaskItemCallbacks

class SubTaskListAdapter(
    private val callbacks: SubtaskItemCallbacks
) : ListAdapter<TypeSubTaskListItem, RecyclerView.ViewHolder>(SubTaskDiffCallback()) {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_task_header, parent, false)
                TaskHeaderViewHolder(view)
            }

            TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
                SubTaskViewHolder(view)
            }

            else -> error("")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (val item = getItem(position)) {
            is TypeSubTaskListItem.HeaderItem -> (holder as TaskHeaderViewHolder).render(item.header)
            is TypeSubTaskListItem.SubTaskItem -> (holder as SubTaskViewHolder).render(
                item.subTaskUI,
                callbacks
            )
        }
    }

    override fun getItemId(position: Int): Long {

        return when (val item = getItem(position)) {
            is TypeSubTaskListItem.HeaderItem -> item.header.hashCode().toLong()
            is TypeSubTaskListItem.SubTaskItem -> item.subTaskUI.id
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TypeSubTaskListItem.HeaderItem -> TYPE_HEADER
            is TypeSubTaskListItem.SubTaskItem -> TYPE_ITEM
        }
    }

    init {
        setHasStableIds(true)
    }
}