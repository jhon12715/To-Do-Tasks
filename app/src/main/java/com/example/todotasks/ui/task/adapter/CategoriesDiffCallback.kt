package com.example.todotasks.ui.task.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todotasks.ui.model.TypeCategoryUI

class CategoriesDiffCallback : DiffUtil.ItemCallback<TypeCategoryUI>() {
    override fun areItemsTheSame(oldItem: TypeCategoryUI, newItem: TypeCategoryUI): Boolean {
        return when {

            oldItem is TypeCategoryUI.CategoryItem &&
                    newItem is TypeCategoryUI.CategoryItem ->
                oldItem.category.id == newItem.category.id

            oldItem is TypeCategoryUI.AddCategory &&
                    newItem is TypeCategoryUI.AddCategory ->
                true

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: TypeCategoryUI, newItem: TypeCategoryUI): Boolean {
        return oldItem == newItem
    }

}