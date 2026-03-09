package com.example.todotasks.ui.subTask.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todotasks.databinding.DialogDeleteTaskBinding
import com.example.todotasks.ui.subTask.SubTaskViewModel

class DialogDeleteSubtask(
    private val idTask: Long,
    private val title: String
) : DialogFragment() {

    private lateinit var binding: DialogDeleteTaskBinding
    private val viewModel: SubTaskViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        val view = builder.setView(binding.root)

        startUI()
        setListeners()

        return builder.create()
    }

    private fun startUI() {

        binding.tvTittle.text = "Eliminar Subtarea"
        binding.tvMessage.text = "seguro que desea borrar la subtarea $title"

    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnAccept.setOnClickListener {
            viewModel.deleteSubtask(idTask)
            dismiss()
        }
    }

    private fun pintUI(title: String, message: String) {
        binding.tvTittle.text = title
        binding.tvMessage.text = message
    }
}