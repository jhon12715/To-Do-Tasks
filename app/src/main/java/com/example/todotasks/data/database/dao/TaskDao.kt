package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.todotasks.data.database.entities.TaskEntity
import com.example.todotasks.data.projection.TaskWithStatsProjection
import com.example.todotasks.domain.model.ParentTaskInfo
import com.example.todotasks.domain.model.Task
import com.example.todotasks.domain.model.TaskPriority
import com.example.todotasks.ui.model.TaskUI
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT t.id, t.task, (SELECT COUNT(*) FROM subtask s WHERE s.idTask = t.id AND s.completed = 1) as completedSubtask, (SELECT COUNT(*) FROM subtask s WHERE s.idTask = t.id) as totalSubTask, isCompleted, priority, date, category FROM task t")
    fun getAllTasks(): Flow<List<TaskWithStatsProjection>>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTask(id: Long): Task

    @Query("SELECT id as taskId, task as taskName FROM task WHERE id = :id")
    suspend fun getParentTaskInfo(id: Long): ParentTaskInfo

    @Upsert
    suspend fun upsertTask(task: TaskEntity): Long

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("UPDATE task SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletedTask(id: Long, isCompleted: Boolean)

}