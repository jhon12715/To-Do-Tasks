package com.example.todotasks.ui.subTask.subtask_list.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemTaskBinding
import com.example.todotasks.ui.model.ScreenMode
import com.example.todotasks.ui.model.SubTaskUI
import com.example.todotasks.ui.subTask.subtask_list.SubtaskItemCallbacks

class SubTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    private val binding = ItemTaskBinding.bind(view)
    private lateinit var item: SubTaskUI
    private lateinit var screenMode: ScreenMode
    private lateinit var callbacks: SubtaskItemCallbacks

    fun render(
        itemToRender: SubTaskUI,
        screenModeToViewHolder: ScreenMode,
        callbacksToViewHolder: SubtaskItemCallbacks
    ) {

        item = itemToRender
        callbacks = callbacksToViewHolder
        screenMode = screenModeToViewHolder

        renderTittle(item.tittle)
        hideUnnecessaryUI()
        renderByScreenMode(screenMode)

    }

    private fun hideUnnecessaryUI() = with(binding) {
        ivEditTask.visibility = View.GONE
        tvTaskCompleted.visibility = View.GONE
        tvDate.visibility = View.GONE
    }

    fun renderTittle(newTittle: String) {
        binding.tvTaskName.text = newTittle
    }

    fun renderByScreenMode(screenMode: ScreenMode) {
        when (screenMode) {
            ScreenMode.NORMAL -> changeScreenModeNormal()
            ScreenMode.DELETE -> changeScreenModeDelete()
            else -> {}
        }
    }

    fun renderIsCompleted(newIsCompleted: Boolean) = with(binding.cbIsCompleted) {
        binding.cbIsCompleted.setOnCheckedChangeListener(null)
        isChecked = newIsCompleted
        setOnCheckedChangeListener { _, isChecked ->
            callbacks.updateSubTaskCompleted(item.id, isChecked)

        }

        applyState(newIsCompleted)
    }

    fun renderIsSelectedToDelete(newIsSelectedToDelete: Boolean) = with(binding.cbIsSelectedToDelete) {
        setOnCheckedChangeListener(null)
        isChecked = newIsSelectedToDelete
        setOnCheckedChangeListener { _, isChecked ->

            deleteModeCallback(isChecked)
        }

    }

    private fun changeScreenModeNormal() {
        binding.cbIsCompleted.isVisible = true
        binding.cbIsSelectedToDelete.isVisible = false

        renderIsCompleted(item.isCompleted)
        setupClicksModeNormal()
    }

    private fun setupClicksModeNormal() {

        binding.cvTaskItem.setOnClickListener { callbacks.editSubTaskForm(item.id) }
        binding.cvTaskItem.setOnLongClickListener {
            callbacks.addSubTaskToDeleteList(item.id)
            true
        }
    }

    private fun changeScreenModeDelete() {

        binding.cbIsCompleted.isVisible = false
        binding.cbIsSelectedToDelete.isVisible = true

        applyOriginalState(binding.root.context)
        renderIsSelectedToDelete(item.isSelectedToDelete)
        setupClicksModeDelete()
    }

    private fun setupClicksModeDelete() = with(binding.cvTaskItem) {

        setOnClickListener {
            val isChecked = !binding.cbIsSelectedToDelete.isChecked
            deleteModeCallback(isChecked)
        }

        setOnLongClickListener {
            true
        }
    }

    private fun deleteModeCallback(isChecked: Boolean){
        if (isChecked) {
            callbacks.addSubTaskToDeleteList(item.id)
        } else {
            callbacks.removeSubTaskToDeleteList(item.id)
        }
    }

    private fun applyState(isCompleted: Boolean) {
        val context = binding.root.context
        if (isCompleted && screenMode == ScreenMode.NORMAL) {

            applyCompletedState(context)

        } else {

            applyOriginalState(context)
        }
    }

    private fun applyCompletedState(context: Context) = with(binding) {
        tvTaskName.paintFlags =
            tvTaskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        cvTaskItem.setCardBackgroundColor(
            ContextCompat.getColor(context, R.color.grey)
        )
    }

    private fun applyOriginalState(context: Context) = with(binding) {

        tvTaskName.paintFlags =
            tvTaskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        cvTaskItem.setCardBackgroundColor(
            ContextCompat.getColor(context, R.color.white)
        )
    }
}