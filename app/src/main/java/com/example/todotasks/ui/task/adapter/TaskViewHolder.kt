package com.example.todotasks.ui.task.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.domain.model.Task
import com.example.todotasks.ui.task.TaskItemCallbacks
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.TaskUI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(
        item: TaskUI,
        callbacks: TaskItemCallbacks
    ) = with(binding) {

        tvTaskName.text = item.task
        tvTaskCompleted.text = item.subTaskCompleted

        if (item.date != null) {
            setDate(item.date)
            tvDate.visibility = View.VISIBLE
        } else {
            tvDate.visibility = View.GONE
        }

        setUpCount(item.totalSubTask)
        setupCheckbox(item, callbacks.updateCompletedTask)
        setupClicks(item, callbacks.editTask, callbacks.openSubTaskActivity, callbacks.deleteTask)

        applyState(item)
    }

    private fun setDate(day: LocalDate) = with(binding) {
        val today = LocalDate.now()
        val dayRest: Long = ChronoUnit.DAYS.between(today, day)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        tvDate.text = when {
            dayRest < 0 -> "Vencida (${day.format(formatter)})"
            dayRest == 0L -> "Hoy"
            dayRest == 1L -> "Mañana"
            dayRest <= 7L -> "Quedan $dayRest días (${day.format(formatter)})"
            else -> {
                day.format(formatter)
            }
        }
    }

    private fun setUpCount(subtasks: Int) = with(binding) {
        if (subtasks == 0) {
            tvTaskCompleted.visibility = View.GONE
        } else {
            tvTaskCompleted.visibility = View.VISIBLE
        }

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
        editTask: (TaskUI) -> Unit,
        openSubTaskActivity: (Long, String) -> Unit,
        deleteTask: (Task) -> Unit
    ) = with(binding) {

        ivEditTask.setOnClickListener {
            editTask(item)
        }

        cvTaskItem.setOnClickListener {
            openSubTaskActivity(item.id, item.task)
        }

        cvTaskItem.setOnLongClickListener {
            val task = Task(item.id, item.task, item.priority, item.isCompleted, item.date)
            deleteTask(task)
            true
        }
    }

    private fun applyState(item: TaskUI) = with(binding) {

        val context = root.context

        if (item.isCompleted) {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            tvTaskCompleted.visibility = View.GONE
            tvDate.visibility = View.GONE

            cvTaskItem.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.itemCompleted)
            )

        } else {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            setUpCount(item.totalSubTask)

            //val color = getPriorityColor(item.priority)

            //cvTaskItem.setCardBackgroundColor(
            //ContextCompat.getColor(context, color)
            //)
        }
    }

    private fun getPriorityColor(priority: TaskPriority): Int =
        when (priority) {
            TaskPriority.ALTA -> R.color.highPriority
            TaskPriority.NORMAL -> R.color.normalPriority
            TaskPriority.BAJA -> R.color.lowPriority
        }

}