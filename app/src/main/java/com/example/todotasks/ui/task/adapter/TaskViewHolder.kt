package com.example.todotasks.ui.task.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.domain.model.Task

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(
        item: Task,
        editTask: (Task) -> Unit,
        openSubTaskActivity: (Task) -> Unit,
        deleteTask: (Task) -> Unit
    ) {

        binding.tvTaskName.text = item.task
        binding.ivEditTask.setOnClickListener {
            editTask(item)
        }
        ///binding.cbTask.isChecked = item.completada
        binding.tvTaskCompleted.text = item.subTaskCompleted
        setListeners(item, openSubTaskActivity, deleteTask)
    }

    private fun setListeners(
        item: Task,
        openSubTaskActivity: (Task) -> Unit,
        deleteTask: (Task) -> Unit
    ) {
        binding.cvTaskItem.setOnClickListener {
            openSubTaskActivity(item)
        }

        binding.cvTaskItem.setOnLongClickListener {
            deleteTask(item)
            true
        }
    }
}