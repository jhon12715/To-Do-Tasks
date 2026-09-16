package com.example.todotasks.ui.task.task_list.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.ui.core.extensions.getAppFormatter
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)
    private lateinit var item: TaskUI
    private lateinit var callbacks: TaskItemCallbacks

    fun render(
        itemTask: TaskUI,
        screenMode: ScreenMode,
        itemCallbacks: TaskItemCallbacks
    ) = with(binding) {

        item = itemTask
        callbacks = itemCallbacks

        renderTittle(item.tittle)
        cbIsCompleted.isVisible = screenMode != ScreenMode.DELETE
        cbIsSelectedToDelete.isVisible = screenMode == ScreenMode.DELETE
        ivEditTask.isVisible = screenMode != ScreenMode.DELETE

        when (screenMode) {
            ScreenMode.NORMAL -> renderModeNormal()
            ScreenMode.DELETE -> renderModeDelete()
            else -> {}
        }
    }

    fun renderTittle(tittle: String) = with(binding) {
        tvTaskName.text = tittle
    }

    private fun renderModeNormal() = with(binding) {

        renderDate(item.date)
        renderSubTasksCompleted(item.subTaskCompleted)
        setupCheckbox()
        setupClicks()

        applyState(item)

    }

    fun renderDate(date: LocalDate?) = with(binding) {

        if (date == null) {
            binding.tvDate.visibility = View.GONE
            return
        } else {
            binding.tvDate.visibility = View.VISIBLE
            tvDate.text = getDateByDiferenceOfToday(date)
        }
    }

    fun getDateByDiferenceOfToday(date: LocalDate): String{
        val today = LocalDate.now()
        val dayRest: Long = ChronoUnit.DAYS.between(today, date)
        val dateFormatted = date.format(getAppFormatter())

        return when {
            dayRest < 0 -> "Vencida (${dateFormatted})"
            dayRest == 0L -> "Hoy"
            dayRest == 1L -> "Mañana"
            dayRest <= 7L -> "Quedan $dayRest días (${dateFormatted})"
            else -> {
                dateFormatted
            }
        }
    }

    fun renderSubTasksCompleted(subTaskCompleted: String) {
        binding.tvTaskCompleted.apply {
            visibility = if (item.totalSubTask == 0) View.GONE else View.VISIBLE
            text = subTaskCompleted
        }
    }

    private fun setupCheckbox() = with(binding) {

        cbIsCompleted.setOnCheckedChangeListener(null)
        renderIscompleted(item.isCompleted)

        cbIsCompleted.setOnCheckedChangeListener { _, isChecked ->
            callbacks.updateCompletedTask(item.id, isChecked)
        }
    }

    fun renderIscompleted(isCompleted: Boolean) {
        binding.cbIsCompleted.isChecked = isCompleted
    }

    private fun setupClicks() = with(binding) {

        ivEditTask.setOnClickListener {
            callbacks.editTask(item)
        }

        cvTaskItem.setOnClickListener {
            callbacks.openSubTaskActivity(item.id, item.tittle)
        }

        cvTaskItem.setOnLongClickListener {
            callbacks.updateScreenMode(ScreenMode.DELETE)
            callbacks.addTaskToDelete(item.id)
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
                ContextCompat.getColor(context, R.color.grey)
            )

        } else {

            tvTaskName.paintFlags =
                tvTaskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            cvTaskItem.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
            )
        }
    }

    private fun renderModeDelete() = with(binding) {
        tvTaskCompleted.visibility = View.GONE
        tvDate.visibility = View.GONE

        setupCheckboxDeleteMode()
        setupClicksDeleteMode()

    }

    private fun setupCheckboxDeleteMode() = with(binding) {

        cbIsSelectedToDelete.setOnCheckedChangeListener(null)
        renderIsSelectedToDelete(item.isSelectedToDelete)

        cbIsSelectedToDelete.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                callbacks.removeTaskToDelete(item.id)
            } else {
                callbacks.addTaskToDelete(item.id)
            }
        }
    }

    fun renderIsSelectedToDelete(isSelectedToDelete: Boolean) {
        binding.cbIsSelectedToDelete.isChecked = isSelectedToDelete
    }

    private fun setupClicksDeleteMode() = with(binding) {

        cvTaskItem.setOnClickListener {
            cbIsSelectedToDelete.isChecked = !cbIsSelectedToDelete.isChecked
        }

        cvTaskItem.setOnLongClickListener {
            true
        }
    }

}