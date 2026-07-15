package com.example.todotasks.ui.model

sealed class TypeCategoryUI {
    data class CategoryItem(val category: CategoryUI, val selected: Boolean = false): TypeCategoryUI()
    data object AddCategory: TypeCategoryUI()
}