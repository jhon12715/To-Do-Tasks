package com.example.todotasks.ui.task.category_form

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.R
import com.example.todotasks.databinding.CategoryTaskSheetBinding
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.core.extensions.getParcelableCompat
import com.example.todotasks.ui.core.extensions.hideKeyboard
import com.example.todotasks.ui.model.CategoryUI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryTaskSheet : BottomSheetDialogFragment() {

    private var _binding: CategoryTaskSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryFormViewModel by viewModels()
    private var category: CategoryUI = CategoryUI()

    companion object {

        private const val CATEGORY_ITEM = "CATEGORY_ITEM"
        fun newInstance(category: CategoryUI): CategoryTaskSheet {
            val taskSheet = CategoryTaskSheet()
            val bundle = Bundle().apply {
                putParcelable(CATEGORY_ITEM, category)
            }

            taskSheet.arguments = bundle
            return taskSheet
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getParcelableCompat<CategoryUI>(CATEGORY_ITEM) ?: CategoryUI()
        viewModel.onEvent(CategoryEvent.OpeningCategoryForm(category))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUI()
        initListeners()
        setFlows()
    }

    private fun startUI() {
        val categoryForm = viewModel.categoryFormState.value
        val title = if (categoryForm.categoryIdForm == 0L) {
            "Añadir nueva categoría"
        } else {
            "Editar categoría"
        }

        binding.tvTittle.text = title
        binding.etCategory.setText(categoryForm.nameCategoryForm)
    }

    private fun initListeners() {
        binding.btnAccept.setOnClickListener {
            viewModel.onEvent(CategoryEvent.UpsertCategory)
        }

        binding.etCategory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onEvent(CategoryEvent.UpdateNameCategoryForm(text.toString()))
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etCategory.setOnFocusChangeListener { view, _ -> view.hideKeyboard() }
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryFormState.map { it.isValid }
                    .debounce { 100 }
                    .distinctUntilChanged()
                    .collect { isValid ->
                        var color = ContextCompat.getColor(requireContext(), R.color.formValid)
                        if (isValid) {
                            binding.btnAccept.text = "Valido"
                        } else {
                            binding.btnAccept.text = "No Valido"
                            color = ContextCompat.getColor(requireContext(), R.color.formNotValid)
                        }

                        binding.btnAccept.isClickable = isValid
                        binding.btnAccept.setBackgroundColor(color)

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