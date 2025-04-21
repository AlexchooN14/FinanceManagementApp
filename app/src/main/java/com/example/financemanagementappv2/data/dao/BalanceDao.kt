package com.example.financemanagementappv2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financemanagementappv2.data.entities.Balance
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {

    @Insert
    suspend fun insert(balance: Balance)

    @Delete
    suspend fun delete(balance: Balance)

    @Query("SELECT * FROM Balance ORDER BY timestamp DESC LIMIT 1")
    fun getLatestBalanceOfUser(): Flow<Balance>

    @Query("SELECT * FROM Balance WHERE timestamp >= :startOfPeriod AND timestamp <= :endOfPeriod ORDER BY timestamp ASC")
    fun getBalanceSnapshotsOfPeriodOfUser(startOfPeriod: Long, endOfPeriod: Long): Flow<List<Balance>>

}