package com.example.todotasks.ui.subTask.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.domain.model.SubTask

class SubTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(
        item: SubTask,
        updateSubTaskCompleted: (Long, Boolean) -> Unit,
        updateSubTaskName: (Long, String) -> Unit,
        deleteSubtask: (Long, String) -> Unit
    ) {
        binding.tvTaskName.text = item.title
        binding.cbTask.isChecked = item.completed
        binding.ivEditTask.visibility = View.GONE
        binding.tvTaskCompleted.visibility = View.GONE
        startListeners(
            item.id,
            item.title,
            updateSubTaskCompleted,
            updateSubTaskName,
            deleteSubtask
        )
    }

    private fun startListeners(
        id: Long,
        subTaskName: String,
        updateSubTaskCompleted: (Long, Boolean) -> Unit,
        updateSubTaskName: (Long, String) -> Unit,
        deleteSubtask: (Long, String) -> Unit
    ) {
        binding.cbTask.setOnClickListener {
            updateSubTaskCompleted(id, binding.cbTask.isChecked)
        }

        binding.cvTaskItem.setOnClickListener { updateSubTaskName(id, subTaskName) }
        binding.cvTaskItem.setOnLongClickListener {
            deleteSubtask(id, subTaskName)
            true
        }
    }
}