package com.example.todotasks.ui.task.task_list.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemBinding
import com.example.todotasks.ui.common.extensions.getAppFormatter
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.TaskUI
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemBinding.bind(view)
    private lateinit var item: TaskUI
    private lateinit var callbacks: TaskItemCallbacks

    fun render(
        itemTask: TaskUI,
        screenMode: ScreenMode,
        itemCallbacks: TaskItemCallbacks
    ) {

        item = itemTask
        callbacks = itemCallbacks

        renderTittle(item.tittle)
        renderComponentsByScreenMode(screenMode)
        renderItemByByScreenMode(screenMode)
    }

    fun renderTittle(tittle: String) = with(binding) {
        tvTaskName.text = tittle
    }

    private fun renderComponentsByScreenMode(screenMode: ScreenMode) = with(binding) {
        cbIsCompleted.isVisible = screenMode != ScreenMode.DELETE
        cbIsSelectedToDelete.isVisible = screenMode == ScreenMode.DELETE
        ivEditTask.isVisible = screenMode != ScreenMode.DELETE
    }

    private fun renderItemByByScreenMode(screenMode: ScreenMode) {
        when (screenMode) {
            ScreenMode.NORMAL -> renderModeNormal()
            ScreenMode.DELETE -> renderModeDelete()
            else -> {}
        }
    }

    private fun renderModeNormal() {

        renderDate(item.date)
        renderSubTasksCompleted(item.subTaskCompleted)
        setupCheckbox()
        setupClicks()

        applyState(item)

    }

    fun renderDate(date: LocalDate?) = with(binding) {

        if (date == null) {
            tvDate.visibility = View.GONE
            return
        } else {
            tvDate.visibility = View.VISIBLE
            tvDate.text = getDateByDiferenceOfToday(date)
        }
    }

    private fun getDateByDiferenceOfToday(date: LocalDate): String {
        val today = LocalDate.now()
        val dayRest: Long = ChronoUnit.DAYS.between(today, date)
        val dateFormatted = date.format(getAppFormatter())
        val context = binding.root.context

        return when {
            dayRest < 0 -> "${context.getString(R.string.time_remaining_expired)} (${dateFormatted})"
            dayRest == 0L -> context.getString(R.string.time_remaining_today)
            dayRest == 1L -> context.getString(R.string.time_remaining_tomorrow)
            dayRest <= 7L -> "${context.getString(R.string.time_remaining)} " +
                    "$dayRest ${
                        context.getString(R.string.time_remaining_days)
                    } (${dateFormatted})"

            else -> dateFormatted
        }
    }

    fun renderSubTasksCompleted(subTaskCompleted: String) {
        binding.tvTaskCompleted.apply {
            visibility = if (item.totalSubTask == 0) View.GONE else View.VISIBLE
            text = subTaskCompleted
        }
    }

    private fun setupCheckbox() = with(binding) {

        renderIsCompleted(item.isCompleted)

        cbIsCompleted.setOnCheckedChangeListener { _, isChecked ->
            callbacks.updateCompletedTask(item.id, isChecked)
        }
    }

    fun renderIsCompleted(isCompleted: Boolean) = with(binding) {
        cbIsCompleted.apply {
            setOnCheckedChangeListener(null)
            cbIsCompleted.isChecked = isCompleted
        }
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
            callbacks.addTaskToDelete(item)
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
        renderIsSelectedToDelete(item.isSelectedToDelete)

        cbIsSelectedToDelete.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                callbacks.removeTaskToDelete(item)
            } else {
                callbacks.addTaskToDelete(item)
            }
        }
    }

    fun renderIsSelectedToDelete(isSelectedToDelete: Boolean) = with(binding) {
        cbIsSelectedToDelete.apply {
            setOnCheckedChangeListener(null)
            isChecked = isSelectedToDelete
        }
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