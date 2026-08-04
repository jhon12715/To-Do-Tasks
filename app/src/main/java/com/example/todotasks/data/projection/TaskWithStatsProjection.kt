package com.example.todotasks.data.projection

import androidx.room.Embedded
import com.example.todotasks.data.database.room.entities.TaskEntity

data class TaskWithStatsProjection(
    @Embedded val task: TaskEntity,
    val completedSubtask: Int,
    val totalSubTask: Int
)