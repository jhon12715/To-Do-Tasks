package com.example.todotasks.ui.task.adapter

import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemCategoryBinding
import com.example.todotasks.ui.core.extensions.cardbackgroundMoreWhite
import com.example.todotasks.ui.model.CategoryUI
import com.example.todotasks.ui.model.TypeCategoryUI

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCategoryBinding.bind(view)

    fun render(
        item: TypeCategoryUI.CategoryItem,
        popupCategoryCallbacks: PopupCategoryCallbacks,
        itemSelected: (Long, Int) -> Unit
    ) = with(binding) {

            setUi(item)
            setListeners(item, popupCategoryCallbacks, itemSelected)

        }

    private fun setUi(item: TypeCategoryUI.CategoryItem) = with(binding) {

        cvCategory.apply {
            if (!item.selected) {
                cardbackgroundMoreWhite(0.5f)
            } else {
                cardbackgroundMoreWhite(0f)
            }
        }
        tvTaskCategory.text = item.category.name
    }

    private fun setListeners(
        item: TypeCategoryUI.CategoryItem,
        popupCategoryCallbacks: PopupCategoryCallbacks,
        itemSelected: (Long, Int) -> Unit
    ) =
        with(binding) {

            cvCategory.setOnClickListener {
                val position = adapterPosition
                itemSelected(item.category.id, position)
            }

            if (layoutPosition != 0) {
                cvCategory.setOnLongClickListener {
                    setCategoryOnLongClickListener(
                        cvCategory,
                        popupCategoryCallbacks,
                        item.category
                    )
                }
            } else {
                cvCategory.setOnLongClickListener(null)
            }
        }

    private fun setCategoryOnLongClickListener(
        cvCategory: CardView,
        popupCategoryCallbacks: PopupCategoryCallbacks,
        item: CategoryUI
    ): Boolean {
        PopupMenuCategory(binding.root.context).show(
            cvCategory, popupCategoryCallbacks, item, layoutPosition
        )
        return true
    }
}