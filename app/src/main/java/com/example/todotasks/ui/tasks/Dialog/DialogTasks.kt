package com.example.todotasks.ui.tasks.Dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotasks.databinding.DialogTasksBinding

class DialogTasks : DialogFragment() {

    private lateinit var binding: DialogTasksBinding
    private lateinit var rvAdapter: TasksDialogAdapter
    private var lista: MutableList<String> = mutableListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTasksBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        setListeners()
        setRvAdapter()

        return builder.create()
    }

    private fun setListeners() {

        binding.button.setOnClickListener {

            dismiss()
        }

        binding.etSubTasks.setOnClickListener {
            lista.add(binding.etSubTasks.text.toString())
            binding.etSubTasks.setText("")
            rvAdapter.lista = lista
            rvAdapter.notifyDataSetChanged()
        }


    }

    private fun setRvAdapter() {

        lista.add("hola")
        lista.add("murillo")

        rvAdapter = TasksDialogAdapter(lista)
        binding.rvSubTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = rvAdapter
        }

    }
}