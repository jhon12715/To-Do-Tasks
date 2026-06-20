package com.example.todotasks.ui.model

import com.example.todotasks.domain.model.SubTask

data class SubTaskUI(
    val list: List<SubTask> = emptyList(),
    val total: Int = 0,
    val completed: Int = 0
) {
    val textSubTasksCompleted: String
        get() = "$completed/$total"
}