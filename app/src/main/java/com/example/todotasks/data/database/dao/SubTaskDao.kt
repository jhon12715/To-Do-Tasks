package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todotasks.data.database.entities.SubTaskEntity
import com.example.todotasks.domain.model.SubTask

@Dao
interface SubTaskDao {

    @Query("SELECT * FROM subTask")
    fun getSubTasks(): List<SubTaskEntity>

    @Insert
    fun insertSubTask(subTask: SubTaskEntity)

    @Query("DELETE FROM subTask WHERE id = :id")
    fun deleteSubTask(id: Long)

    @Query("UPDATE subTask SET title = :title WHERE id = :id")
    fun updateTitleSubTask(id: Long, title: String)

    @Query("UPDATE subTask SET completed = :completed WHERE id = :id")
    fun updateCompletedSubTask(id: Long, completed: Boolean)

}