package com.example.todotasks.ui.task.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemAddCategoryBinding

class AddCategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemAddCategoryBinding.bind(view)

    fun render(openAddCategoryDialog:() -> Unit?) = with(binding){
        fabtnAddCategory.setOnClickListener { openAddCategoryDialog()
            }

    }

}