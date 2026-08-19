package com.example.todotasks.ui.subTask.subtask_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.ui.core.adapter.TaskHeaderViewHolder
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TypeSubTaskListItem
import com.example.todotasks.ui.subTask.subtask_list.SubtaskItemCallbacks

class SubTaskListAdapter(
    private val callbacks: SubtaskItemCallbacks
) : ListAdapter<TypeSubTaskListItem, RecyclerView.ViewHolder>(SubTaskDiffCallback()) {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    private var screenMode = ScreenMode.NORMAL

    fun setScreenMode(newScreenMode: ScreenMode) {
        screenMode = newScreenMode
        notifyItemRangeChanged(0, itemCount, PayloadSubtask.ScreenModeChanged(newScreenMode))
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

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        val latestPayload = payloads.lastOrNull()

        if (latestPayload == null || item !is TypeSubTaskListItem.SubTaskItem) {
            onBindViewHolder(holder, position)
            return
        }

        val subTaskHolder = holder as SubTaskViewHolder

        when (val latestPayloadSubtask = latestPayload as PayloadSubtask) {

            is PayloadSubtask.ScreenModeChanged -> {
                val screenMode = latestPayloadSubtask.newScreenMode
                subTaskHolder.renderByScreenMode(screenMode)
            }

            is PayloadSubtask.TittleSubTaskChange -> subTaskHolder.renderTittle(latestPayloadSubtask.newTittle)

            is PayloadSubtask.IsCompletedSubTaskChange -> subTaskHolder.renderIsCompleted(latestPayloadSubtask.newIsCompleted)

            is PayloadSubtask.IsSelectedToDeleteSubTaskChange -> subTaskHolder.renderIsSelectedToDelete(latestPayloadSubtask.isSelectedToDelete)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (val item = getItem(position)) {
            is TypeSubTaskListItem.HeaderItem -> (holder as TaskHeaderViewHolder).render(item.header)
            is TypeSubTaskListItem.SubTaskItem -> {
                (holder as SubTaskViewHolder).render(
                    item.subTaskUI, screenMode, callbacks
                )
            }
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