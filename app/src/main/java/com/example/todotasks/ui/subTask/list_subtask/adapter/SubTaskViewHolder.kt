package com.example.todotasks.ui.subTask.list_subtask.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.subTask.SubtaskItemCallbacks

class SubTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(
        item: SubTaskUI,
        callbacks: SubtaskItemCallbacks
    ) = with(binding) {
        tvTaskName.text = item.title
        ivEditTask.visibility = View.GONE
        tvTaskCompleted.visibility = View.GONE
        tvDate.visibility = View.GONE

        setupCheckbox(item, callbacks.updateSubTaskCompleted)
        setupClicks(item, callbacks.editSubTaskForm, callbacks.deleteSubtask)
        applyState(item)

    }

    private fun setupClicks(item: SubTaskUI, editSubTaskForm: (SubTaskUI) -> Unit,
                            deleteSubtask: (Long, String) -> Unit) {
        binding.cvTaskItem.setOnClickListener { editSubTaskForm(item) }
        binding.cvTaskItem.setOnLongClickListener {
            deleteSubtask(item.id, item.title)
            true
        }
    }

    private fun setupCheckbox(item: SubTaskUI, updateSubTaskCompleted: (Long, Boolean) -> Unit) = with(binding) {

        cbTask.setOnCheckedChangeListener(null)
        cbTask.isChecked = item.completed

        cbTask.setOnCheckedChangeListener { _, isChecked ->
            updateSubTaskCompleted(item.id, isChecked)
        }
    }

    private fun applyState(item: SubTaskUI) = with(binding) {

        val context = root.context

        if (item.completed) {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            cvTaskItem.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.grey)
            )

        } else {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            //val color = getPriorityColor(item.priority)
            cvTaskItem.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
            )
        }
    }

    private fun getPriorityColor(priority: TaskPriority): Int =
        when (priority) {
            TaskPriority.ALTA -> R.color.highPriority
            TaskPriority.NORMAL -> R.color.normalPriority
            TaskPriority.BAJA -> R.color.lowPriority
        }
}