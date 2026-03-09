package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.domain.model.SubTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {

    @Query("SELECT * FROM subTask WHERE idTask = :idTask ORDER BY id ASC")
    fun getSubTasks(idTask: Long): Flow<List<SubTask>>

    @Insert
    suspend fun insertSubTask(subTask: SubTaskEntity): Long

    @Query("DELETE FROM subTask WHERE id = :id")
    suspend fun deleteSubTask(id: Long)

    @Query("UPDATE subTask SET title = :title WHERE id = :id")
    suspend fun updateTitleSubTask(id: Long, title: String)

    @Query("UPDATE subTask SET completed = :completed WHERE id = :id")
    suspend fun updateCompletedSubTask(id: Long, completed: Boolean)

}