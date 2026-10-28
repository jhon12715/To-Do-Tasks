package com.example.todotasks.ui.task.category_form

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.todotasks.R
import com.example.todotasks.databinding.CategoryTaskSheetBinding
import com.example.todotasks.domain.model.Category
import com.example.todotasks.ui.common.extensions.getParcelableCompat
import com.example.todotasks.ui.common.extensions.hideKeyboard
import com.example.todotasks.ui.common.extensions.setSettingsEditTextOfForm
import com.example.todotasks.ui.common.string_resource.StringProvider
import com.example.todotasks.ui.model.CategoryUI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryTaskSheet : BottomSheetDialogFragment() {

    private var _binding: CategoryTaskSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryFormViewModel by viewModels()
    private var category: CategoryUI = CategoryUI()
    private var toast: Toast? = null

    @Inject
    lateinit var stringProvider: StringProvider

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
        setSettingsComponents()
        val categoryForm = viewModel.categoryFormState.value

        val tittle = if (categoryForm.categoryIdForm == 0L) {
            getString(R.string.add_new_category_form)
        } else {
            getString(R.string.edit_category_form)
        }

        binding.tvTittle.text = tittle
        binding.tiedCategoryName.setText(categoryForm.nameCategoryForm)
    }

    private fun setSettingsComponents() {
        setSettingsEditTextOfForm(
            binding.tilCategoryName,
            binding.tiedCategoryName,
            Category.CATEGORY_TITTLE_MAX_LENGTH
        )
    }

    private fun initListeners() {
        binding.btnAccept.setOnClickListener {
            viewModel.onEvent(CategoryEvent.UpsertCategory)
        }

        binding.tiedCategoryName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onEvent(CategoryEvent.UpdateNameCategoryForm(text.toString()))
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.tiedCategoryName.setOnFocusChangeListener { view, _ -> view.hideKeyboard() }
    }

    private fun setFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resultEvent.collect { resultEvent ->

                    showToast(stringProvider.getResultEventResource(resultEvent))

                    if (resultEvent.isSuccess) dismiss()

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
                            binding.btnAccept.text = getString(R.string.btn_valid)
                        } else {
                            binding.btnAccept.text = getString(R.string.btn_no_valid)
                            color = ContextCompat.getColor(requireContext(), R.color.formNotValid)
                        }

                        binding.btnAccept.isClickable = isValid
                        binding.btnAccept.setBackgroundColor(color)

                    }
            }
        }
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast!!.show()
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