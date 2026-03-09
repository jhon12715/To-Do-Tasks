package com.example.todotasks.ui.task.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.task.model.TaskUI

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(
        item: TaskUI,
        editTask: (Long, String) -> Unit,
        openSubTaskActivity: (Long, String) -> Unit,
        deleteTask: (Long, String) -> Unit,
        updateCompletedTask: (Long, Boolean) -> Unit
    ) = with(binding) {

        tvTaskName.text = item.task
        tvTaskCompleted.text = item.subTaskCompleted

        setupCheckbox(item, updateCompletedTask)
        setupClicks(item, editTask, openSubTaskActivity, deleteTask)

        applyState(item)
    }

    private fun setupCheckbox(
        item: TaskUI,
        updateCompletedTask: (Long, Boolean) -> Unit
    ) = with(binding) {

        cbTask.setOnCheckedChangeListener(null)
        cbTask.isChecked = item.isCompleted

        cbTask.setOnCheckedChangeListener { _, isChecked ->
            updateCompletedTask(item.id, isChecked)
        }
    }

    private fun setupClicks(
        item: TaskUI,
        editTask: (Long, String) -> Unit,
        openSubTaskActivity: (Long, String) -> Unit,
        deleteTask: (Long, String) -> Unit
    ) = with(binding) {

        ivEditTask.setOnClickListener {
            editTask(item.id, item.task)
        }

        cvTaskItem.setOnClickListener {
            openSubTaskActivity(item.id, item.task)
        }

        cvTaskItem.setOnLongClickListener {
            deleteTask(item.id, item.task)
            true
        }
    }

    private fun applyState(item: TaskUI) = with(binding) {

        val context = root.context

        if (item.isCompleted) {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            cvTaskItem.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.itemCompleted)
            )

        } else {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            cvTaskItem.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.itemDefault)
            )
        }
    }
}