package com.example.todotasks.ui.core.dialog_base_commit_action

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.todotasks.databinding.DialogDeleteTaskBinding
import com.example.todotasks.ui.core.extensions.applyWindowApparence
import com.example.todotasks.ui.core.extensions.backgroundMoreWhite

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

open class DialogBaseCommitAction() : DialogFragment() {

    protected lateinit var binding: DialogDeleteTaskBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteTaskBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        startUI()
        setListeners()
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        applyWindowApparence()
    }

    open fun startUI() {
        binding.clParent.backgroundMoreWhite(0.9f)

        binding.tvTittle.text = "Aviso"
        binding.tvMessage.text = "¿Seguro que desea realizar la acción?"

    }

    protected open fun setListeners() {}
}