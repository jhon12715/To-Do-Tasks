package com.example.todotasks.ui.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.R
import com.example.todotasks.ui.model.TypeCategoryUI

class CategoriesListAdapter(private val openAddCategoryDialog:() -> Unit, private val updateCategorySelected:(Long) -> Unit) :
    ListAdapter<TypeCategoryUI, RecyclerView.ViewHolder>(CategoriesDiffCallback()) {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_ADD = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            TYPE_CATEGORY -> {
                val view = inflater.inflate(R.layout.item_category, parent, false)
                CategoriesViewHolder(view)
            }

            TYPE_ADD -> {
                val view = inflater.inflate(R.layout.item_add_category, parent, false)
                AddCategoryViewHolder(view)
            }

            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {

            is TypeCategoryUI.CategoryItem ->
                (holder as CategoriesViewHolder).render(item, updateCategorySelected)

            is TypeCategoryUI.AddCategory ->
                (holder as AddCategoryViewHolder).render(openAddCategoryDialog)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {

            is TypeCategoryUI.CategoryItem -> TYPE_CATEGORY

            is TypeCategoryUI.AddCategory -> TYPE_ADD
        }
    }

}