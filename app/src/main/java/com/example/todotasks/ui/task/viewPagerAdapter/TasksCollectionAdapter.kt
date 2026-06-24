package com.example.todotasks.ui.task.viewPagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todotasks.ui.task.TaskActivity
import com.example.todotasks.ui.task.TaskViewModel
import com.example.todotasks.ui.task.viewPagerAdapter.TasksFragment.Companion.COLLECTION_POSITION

/**
 * Project: To Do Tasks
 * Created by: Jhon
 */

class TasksCollectionAdapter(activity: TaskActivity) : FragmentStateAdapter(activity) {

    private var totalCategories: Int = 0

    fun submitCount(totalCategories: Int) {
        this.totalCategories = totalCategories
        //notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 12

    override fun createFragment(position: Int): Fragment {
        return TasksFragment.newInstance(position)
    }
}