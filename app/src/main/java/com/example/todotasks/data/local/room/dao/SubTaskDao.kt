package com.example.todotasks.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.todotasks.data.local.room.entities.SubTaskEntity
import com.example.todotasks.domain.model.SubTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {

    @Query("SELECT * FROM subTask WHERE idTask = :idTask")
    fun getAllSubTasks(idTask: Long): Flow<List<SubTaskEntity>>

    @Upsert
    suspend fun upsertSubTask(subTask: SubTaskEntity)

    @Query("DELETE FROM subTask WHERE id IN (:listSubTaskId)")
    suspend fun deleteSubTask(listSubTaskId: List<Long>)

    @Query("UPDATE subTask SET completed = :completed WHERE id = :id")
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)

    @Query("SELECT EXISTS (SELECT * FROM subTask WHERE title = :name AND id != :subTaskId AND idTask = :taskId COLLATE NOCASE)")
    suspend fun subTaskExists(name: String, subTaskId: Long, taskId: Long): Boolean

    @Query("SELECT * FROM subTask WHERE id = :subTaskId")
    suspend fun getSubtaskById(subTaskId: Long): SubTaskEntity

}