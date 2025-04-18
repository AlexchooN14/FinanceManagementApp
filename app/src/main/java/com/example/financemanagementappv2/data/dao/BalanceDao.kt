package com.example.financemanagementappv2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financemanagementappv2.data.entities.Balance

@Dao
interface BalanceDao {

    @Insert
    suspend fun insert(balance: Balance)

    @Delete
    suspend fun delete(balance: Balance)

    @Query("SELECT * FROM Balance")
    fun getBalanceOfUser(): LiveData<Balance>

}