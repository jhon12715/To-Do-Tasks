package com.example.todotasks.ui.task.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.databinding.CategoryTaskSheetBinding
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.task.TaskUiEvent
import com.example.todotasks.ui.task.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class CategoryTaskSheet : BottomSheetDialogFragment() {

    private var _binding: CategoryTaskSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setFlows()
    }

    private fun initListeners() {
        binding.btnAccept.setOnClickListener {
            val category: String = binding.etCategory.text.toString()
            viewModel.onEvent(TaskUiEvent.UpsertCategory(category))
        }
    }

    private fun setFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultEvent.collect { result ->
                    when (result) {
                        is ResultEvent.Success -> dismiss()
                        is ResultEvent.Error -> {}
                    }

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}