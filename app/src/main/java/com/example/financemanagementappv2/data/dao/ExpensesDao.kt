package com.example.financemanagementappv2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financemanagementappv2.data.entities.Expenses
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpensesDao {

    @Insert
    suspend fun insertAll(vararg expenses: Expenses)

    @Delete
    suspend fun delete(expense: Expenses)

    @Query("SELECT * FROM Expenses")
    fun getAllExpensesOfUser(): Flow<List<Expenses>>

    @Query("SELECT * FROM Expenses ORDER BY date DESC LIMIT 1")
    fun getLastExpenseOfUser(): Flow<Expenses>

    @Query("SELECT * FROM Expenses WHERE date >= :startOfPeriod AND date <= :endOfPeriod ORDER BY date ASC")
    fun getAllExpensesOfPeriodOfUser(startOfPeriod: Long, endOfPeriod: Long): Flow<List<Expenses>>

}