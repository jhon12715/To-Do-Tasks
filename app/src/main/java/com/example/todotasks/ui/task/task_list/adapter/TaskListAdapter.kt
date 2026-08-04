package com.example.todotasks.ui.task.task_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.ui.core.adapter.TaskHeaderViewHolder
import com.example.todotasks.ui.task.TaskItemCallbacks
import com.example.todotasks.ui.model.TypeTaskListItem

class TaskListAdapter(
    private val callbacks: TaskItemCallbacks
) : ListAdapter<TypeTaskListItem, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    companion object {
        const val TYPE_HEADER_TASK = 0
        const val TYPE_ITEM_TASK = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        println("entraaaaa")
        return when (viewType) {
            TYPE_HEADER_TASK -> {
                val view = inflater.inflate(R.layout.item_task_header, parent, false)
                TaskHeaderViewHolder(view)
            }

            TYPE_ITEM_TASK -> {
                val view = inflater.inflate(R.layout.item_task, parent, false)
                TaskViewHolder(view)
            }

            else -> error("Unknown view type")
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        println("item3: ${getItem(position)}")
        when (val item = getItem(position)) {
            is TypeTaskListItem.HeaderItem -> {
                (holder as TaskHeaderViewHolder).render(item.header)
            }

            is TypeTaskListItem.TaskItem -> {
                (holder as TaskViewHolder).render(item.task, callbacks)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return when (getItemViewType(position)) {
            TYPE_HEADER_TASK -> -getItem(position).hashCode().toLong()
            TYPE_ITEM_TASK -> (super.getItem(position) as TypeTaskListItem.TaskItem).task.id
            else -> -1000L
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TypeTaskListItem.HeaderItem -> TYPE_HEADER_TASK
            is TypeTaskListItem.TaskItem -> TYPE_ITEM_TASK
        }
    }

    init {
        setHasStableIds(true)
    }

}