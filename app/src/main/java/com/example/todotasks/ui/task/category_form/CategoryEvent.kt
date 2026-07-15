package com.example.todotasks.ui.task.category_form

import com.example.todotasks.ui.model.CategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

sealed class CategoryEvent(){
    data class OpeningCategoryForm(val categoryUI: CategoryUI): CategoryEvent()
    data class UpdateNameCategoryForm(val name: String): CategoryEvent()
    data object UpsertCategory: CategoryEvent()
}