package com.example.todotasks.ui.task.model

import com.example.todotasks.domain.model.SubTask

data class SubTaskUi(
    val list: List<SubTask> = emptyList(),
    val total: Int = 0,
    val completed: Int = 0
)