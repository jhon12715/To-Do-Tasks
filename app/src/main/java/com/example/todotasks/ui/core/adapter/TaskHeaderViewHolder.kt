package com.example.todotasks.ui.core.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todotasks.databinding.ItemTaskHeaderBinding

/**
 * Project: To Do Tasks
 * Created by: Jhon

 */
class TaskHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskHeaderBinding.bind(view)

    fun render(header: String) {
        binding.tvGroupBy.text = header
    }
}