package com.example.todotasks.ui.task.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.databinding.ItemCategoryBinding
import com.example.todotasks.ui.model.TypeCategoryUI

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCategoryBinding.bind(view)

    fun render(item: TypeCategoryUI.CategoryItem, itemSelected:(Long) -> Unit) = with(binding) {

        setUi(item)
        setListeners(item, itemSelected)

    }

    private fun setUi(item: TypeCategoryUI.CategoryItem) = with(binding) {
        val context = root.context

        var color = ContextCompat.getColor(context, R.color.white)
        var textColor = ContextCompat.getColor(context, R.color.black)

        if (item.selected) {
            color = ContextCompat.getColor(context, R.color.categorySelected)
            textColor = ContextCompat.getColor(context, R.color.white)
        }

        cvCategory.setCardBackgroundColor(color)
        tvTaskCategory.setTextColor(textColor)

        tvTaskCategory.text = item.category.name
    }

    private fun setListeners(item: TypeCategoryUI.CategoryItem, itemSelected:(Long) -> Unit) = with(binding) {

        binding.cvCategory.setOnClickListener {

            itemSelected(item.category.id)
        }
    }

}