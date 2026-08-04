package com.example.todotasks.data.projection

import androidx.room.Embedded
import com.example.todotasks.data.local.room.entities.TaskEntity

data class TaskWithStatsProjection(
    @Embedded val task: TaskEntity,
    val completedSubtask: Int,
    val totalSubTask: Int
)