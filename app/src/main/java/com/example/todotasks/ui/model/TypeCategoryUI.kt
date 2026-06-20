package com.example.todotasks.ui.model

import com.example.todotasks.domain.model.Category

sealed class TypeCategoryUI {
    data class CategoryItem(val category: Category, val selected: Boolean = false): TypeCategoryUI()
    data object AddCategory: TypeCategoryUI()
}