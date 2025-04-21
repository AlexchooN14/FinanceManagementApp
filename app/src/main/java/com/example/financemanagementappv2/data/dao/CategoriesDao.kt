package com.example.financemanagementappv2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financemanagementappv2.data.enums.CategoryFinancialType
import com.example.financemanagementappv2.data.entities.Categories
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Insert
    suspend fun insertAll(vararg categories: Categories)

    @Delete
    suspend fun delete(category: Categories)

    @Query("SELECT * FROM Categories WHERE type = :financialType")
    fun getAllCategoriesByFinancialType(financialType: CategoryFinancialType): Flow<List<Categories>>

    @Query("SELECT * FROM Categories")
    fun getAllCategories(): Flow<List<Categories>>

    @Query("SELECT * FROM Categories WHERE id = :id")
    fun getCategoryByCategoryId(id: Long): Categories

}