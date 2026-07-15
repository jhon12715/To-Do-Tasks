package com.example.todotasks.ui.task.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.todotasks.databinding.PopupCustomBinding
import com.example.todotasks.domain.model.Category
import com.example.todotasks.ui.model.CategoryUI

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class PopupMenuCategory(context: Context) {

    private val binding = PopupCustomBinding.inflate(LayoutInflater.from(context))

    private val popup: PopupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    ).apply {
        isOutsideTouchable = true
    }

    fun show(
        anchor: View,
        popupCategoryCallbacks: PopupCategoryCallbacks,
        category: CategoryUI,
        position: Int
    ) {

        binding.tvEdit.setOnClickListener {
            popupCategoryCallbacks.onEdit(category)
            popup.dismiss()
        }

        binding.tvDelete.setOnClickListener {
            popupCategoryCallbacks.onDelete(category)
            popup.dismiss()
        }

        popup.showAsDropDown(anchor)
    }

}
