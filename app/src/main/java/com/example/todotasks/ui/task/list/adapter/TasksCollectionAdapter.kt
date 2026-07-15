package com.example.todotasks.ui.task.list.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todotasks.ui.model.TypeCategoryUI
import com.example.todotasks.ui.task.TaskActivity
import com.example.todotasks.ui.task.list.TaskFragment

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class TasksCollectionAdapter(activity: TaskActivity) : FragmentStateAdapter(activity) {

    private var categories: List<TypeCategoryUI.CategoryItem> = emptyList()

    fun submitList(newListCategories: List<TypeCategoryUI.CategoryItem>) {
        this.categories = newListCategories
    }

    override fun getItemCount(): Int = categories.size

    override fun getItemId(position: Int): Long {
        return categories[position].category.id
    }
    override fun containsItem(itemId: Long): Boolean {
        return categories.any { it.category.id == itemId }
    }

    override fun createFragment(position: Int): Fragment {
        return TaskFragment.newInstance(categories[position].category.id)
    }
}