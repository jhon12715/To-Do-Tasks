package com.example.todotasks.ui.subTask

import androidx.fragment.app.activityViewModels
import com.example.todotasks.ui.core.dialog_base_commit_action.DialogBaseCommitAction
import com.example.todotasks.ui.subTask.subtask_list.SubTaskUiEvent
import com.example.todotasks.ui.subTask.subtask_list.SubTaskViewModel

class DialogDeleteSubtaskCommit() : DialogBaseCommitAction() {

    private val viewModel: SubTaskViewModel by activityViewModels()

    override fun setListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAccept.setOnClickListener {
            viewModel.onEvent(SubTaskUiEvent.CommitSubTaskDelete)
            dismiss()
        }
    }
}