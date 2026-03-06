package com.example.todotasks.ui.subTask.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todotasks.databinding.DialogTasksBinding
import com.example.todotasks.ui.subTask.SubTaskViewModel

class DialogSubTask(private val idSubTask: Long = 0,private val idTask: Long = 0, private val subTaskName: String = "") :
    DialogFragment() {

    private lateinit var binding: DialogTasksBinding
    private val viewModel: SubTaskViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTasksBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        startUI()
        setListeners()

        return builder.create()
    }

    private fun startUI() {
        if (idSubTask == 0L) {
            binding.tvTittle.text = "Añadir Subtarea"
        } else {
            binding.tvTittle.text = "Editar Subtarea"
            binding.etTask.setText(subTaskName)
        }
    }

    private fun setListeners() {
        binding.btnFinish.setOnClickListener {
            val subTaskName = binding.etTask.text.toString()

            if (idSubTask == 0L) {
                if (viewModel.addSubTask(idTask, subTaskName)) {
                    dismiss()
                } else {

                }
            } else {
                if (viewModel.updateSubTaskName(idSubTask, subTaskName)) {
                    dismiss()
                }
            }
        }
    }


}