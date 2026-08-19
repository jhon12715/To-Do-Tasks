package com.example.todotasks.ui.task.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.example.todotasks.databinding.DialogDeleteTaskBinding
import com.example.todotasks.ui.core.dialog_base_commit_action.DialogBaseCommitAction
import com.example.todotasks.ui.task.task_list.TaskListViewModel
import com.example.todotasks.ui.task.task_list.TaskUiEvent

class DialogDeleteTaskCommit() : DialogBaseCommitAction() {

    private val viewModel: TaskListViewModel by activityViewModels()



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setListeners()

        return builder.create()
    }

    override fun setListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAccept.setOnClickListener {
            viewModel.onEvent(TaskUiEvent.CommitTaskToDelete)
            dismiss()
        }
    }
}