package com.example.todotasks.ui.subTask.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.domain.model.SubTask

class SubTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(
        item: SubTask,
        updateSubTaskCompleted: (Long, Boolean) -> Unit,
        updateSubTaskName: (Long, String) -> Unit,
        deleteSubtask: (Long, String) -> Unit
    ) = with(binding) {
        tvTaskName.text = item.title
        cbTask.isChecked = item.completed
        ivEditTask.visibility = View.GONE
        tvTaskCompleted.visibility = View.GONE

        setupCheckbox(item, updateSubTaskCompleted)
        setupClicks(item, updateSubTaskName, deleteSubtask)
        applyState(item)

    }

    private fun setupClicks(item: SubTask, updateSubTaskName: (Long, String) -> Unit,
                            deleteSubtask: (Long, String) -> Unit) {
        binding.cvTaskItem.setOnClickListener { updateSubTaskName(item.id, item.title) }
        binding.cvTaskItem.setOnLongClickListener {
            deleteSubtask(item.id, item.title)
            true
        }
    }

    private fun setupCheckbox(item: SubTask, updateSubTaskCompleted: (Long, Boolean) -> Unit) = with(binding) {

        cbTask.setOnCheckedChangeListener(null)
        cbTask.isChecked = item.completed

        cbTask.setOnCheckedChangeListener { _, isChecked ->
            updateSubTaskCompleted(item.id, isChecked)
        }
    }

    private fun applyState(item: SubTask) = with(binding) {

        val context = root.context

        if (item.completed) {

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