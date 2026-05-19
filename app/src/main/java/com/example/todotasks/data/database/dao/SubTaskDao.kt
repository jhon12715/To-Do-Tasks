package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.domain.model.SubTask
import com.example.todotasks.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {

    @Query("SELECT * FROM subTask WHERE idTask = :idTask")
    fun getAllSubTasks(idTask: Long): Flow<List<SubTask>>

    @Insert
    suspend fun insertSubTask(subTask: SubTaskEntity)

    @Query("DELETE FROM subTask WHERE id = :id")
    suspend fun deleteSubTask(id: Long)

    @Query("UPDATE subTask SET title = :title, priority = :priority WHERE id = :id")
    suspend fun updateSubTask(id: Long, title: String, priority: TaskPriority)

    @Query("UPDATE subTask SET completed = :completed WHERE id = :id")
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)

}