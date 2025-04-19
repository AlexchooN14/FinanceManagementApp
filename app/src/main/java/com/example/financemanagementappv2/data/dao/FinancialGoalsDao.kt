package com.example.financemanagementappv2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financemanagementappv2.data.entities.FinancialGoals
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialGoalsDao {

    @Insert
    suspend fun insertAll(vararg financialGoals: FinancialGoals)

    @Delete
    suspend fun delete(financialGoal: FinancialGoals)

    @Query("SELECT * FROM FinancialGoals")
    fun getAllFinancialGoalsOfUser(): Flow<List<FinancialGoals>>

}