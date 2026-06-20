package com.example.todotasks.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todotasks.data.database.entities.CategoryEntity
import com.example.todotasks.domain.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao{
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<Category>>

    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
}