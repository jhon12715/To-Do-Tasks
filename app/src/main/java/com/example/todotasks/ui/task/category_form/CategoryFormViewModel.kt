package com.example.todotasks.ui.task.category_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.domain.model.Category
import com.example.todotasks.domain.usecase.CategoryExistsUseCase
import com.example.todotasks.domain.usecase.UpsertCategoryUseCase
import com.example.todotasks.ui.core.ResultEvent
import com.example.todotasks.ui.model.CategoryUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

@HiltViewModel
class CategoryFormViewModel @Inject constructor(
    private val categoryExistsUseCase: CategoryExistsUseCase,
    private val upsertCategoryUseCase: UpsertCategoryUseCase
) : ViewModel() {

    //Result
    private val _resultEvent: MutableSharedFlow<ResultEvent> = MutableSharedFlow(replay = 0)
    val resultEvent: SharedFlow<ResultEvent> = _resultEvent

    //CategoryForm

    private val _categoryFormState: MutableStateFlow<CategoryFormState> = MutableStateFlow(
        CategoryFormState()
    )
    val categoryFormState: StateFlow<CategoryFormState> = _categoryFormState

    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OpeningCategoryForm -> openingCategoryForm(event.categoryUI)
            is CategoryEvent.UpdateNameCategoryForm -> updateNameCategoryForm(event.name)
            CategoryEvent.UpsertCategory -> upsertCategory()
        }
    }

    private fun openingCategoryForm(categoryUI: CategoryUI) {
        _categoryFormState.update {
            it.copy(
                categoryIdForm = categoryUI.id,
                nameCategoryForm = categoryUI.name,
                isValid = categoryUI.name.isNotEmpty()
            )
        }
    }

    private fun updateNameCategoryForm(name: String) {

        viewModelScope.launch {
            _categoryFormState.update {
                it.copy(
                    nameCategoryForm = name,
                    isValid = !categoryExistsUseCase(name, categoryFormState.value.categoryIdForm)
                )
            }
        }

    }

    private fun upsertCategory() {
        viewModelScope.launch {
            val categoryForm = categoryFormState.value
            val newCategory = Category(
                id = categoryForm.categoryIdForm,
                name = categoryForm.nameCategoryForm
            )
            _resultEvent.emit(upsertCategoryUseCase(newCategory))
        }
    }


}