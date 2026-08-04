package com.example.todotasks.data.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.todotasks.data.database.room.entities.CategoryEntity
import com.example.todotasks.domain.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao{
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<Category>>

    @Upsert
    suspend fun upsertCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT EXISTS(SELECT * FROM category WHERE name = :name AND id != :id COLLATE NOCASE)")
    suspend fun categoryExists(name: String, id: Long): Boolean
}