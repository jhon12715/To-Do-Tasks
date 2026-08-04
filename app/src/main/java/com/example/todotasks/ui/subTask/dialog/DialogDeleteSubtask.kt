package com.example.todotasks.ui.subTask.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todotasks.databinding.DialogDeleteTaskBinding
import com.example.todotasks.ui.core.extensions.applyWindowApparence
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite
import com.example.todotasks.ui.subTask.SubTaskUiEvent
import com.example.todotasks.ui.subTask.list_subtask.SubTaskViewModel

class DialogDeleteSubtask() : DialogFragment() {

    private lateinit var binding: DialogDeleteTaskBinding
    private val viewModel: SubTaskViewModel by activityViewModels()
    private var subTaskId = 0L
    private var tittle: String = ""

    companion object{
        const val SUB_TASK_ID = "SUB_TASK_ID"
        const val TITTLE = "TITTLE"
        fun newInstance(subTaskId: Long, tittle: String): DialogDeleteSubtask =
            DialogDeleteSubtask().apply {
                arguments = Bundle().apply {
                    putLong(SUB_TASK_ID, subTaskId)
                    putString(TITTLE, tittle)
                }
            }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        getBundleArguments()
        startUI()
        setListeners()

        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        applyWindowApparence()
    }

    private fun getBundleArguments(){
        arguments?.apply {
            subTaskId = getLong(SUB_TASK_ID, 0L)
            tittle = getString(TITTLE, "")
        }
    }

    private fun startUI() {
        binding.clParent.backgroundMoreWhite(0.9f)

        binding.tvTittle.text = "Eliminar Subtarea"
        binding.tvMessage.text = "seguro que desea borrar la subtarea $tittle"

    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAccept.setOnClickListener {
            viewModel.onEvent(SubTaskUiEvent.DeleteSubTask(subTaskId))
            dismiss()
        }
    }
}