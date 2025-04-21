package com.example.financemanagementappv2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financemanagementappv2.data.entities.Incomes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface IncomesDao {

    @Insert
    suspend fun insertAll(income: Incomes)

    @Delete
    suspend fun delete(income: Incomes)

    @Query("SELECT * FROM Incomes")
    fun getAllIncomesOfUser(): Flow<List<Incomes>>

    @Query("SELECT * FROM Incomes ORDER BY date DESC LIMIT 1")
    fun getLastIncomeOfUser(): Flow<Incomes>

    @Query("SELECT * FROM Incomes WHERE date >= :startOfPeriod AND date <= :endOfPeriod ORDER BY date ASC")
    fun getAllIncomesOfPeriodOfUser(startOfPeriod: Long, endOfPeriod: Long): Flow<List<Incomes>>

    suspend fun getTotalSum(): Double {
        return getAllIncomesOfUser().first().sumOf { income -> income.amount }
    }

}