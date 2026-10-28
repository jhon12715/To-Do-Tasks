package com.example.todotasks.ui.common.dialog_base_commit_action

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.todotasks.R
import com.example.todotasks.databinding.DialogDeleteBinding
import com.example.todotasks.ui.common.extensions.applyWindowApparence
import com.example.todotasks.ui.common.extensions.backgroundMoreWhite

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

open class DialogBaseCommitAction() : DialogFragment() {

    protected lateinit var binding: DialogDeleteBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteBinding.inflate(layoutInflater)
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

        binding.tvTittle.text = getString(R.string.dialog_commit_action_tittle)
        binding.tvMessage.text = getString(R.string.dialog_commit_action_message)

    }

    protected open fun setListeners() {}
}