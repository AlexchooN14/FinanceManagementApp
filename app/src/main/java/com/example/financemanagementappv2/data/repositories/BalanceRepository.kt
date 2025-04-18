package com.example.financemanagementappv2.data.repositories

import androidx.lifecycle.LiveData
import com.example.financemanagementappv2.data.dao.BalanceDao
import com.example.financemanagementappv2.data.entities.Balance

class BalanceRepository(private val balanceDao: BalanceDao) {

    suspend fun insert(balance: Balance) {
        balanceDao.insert(balance)
    }

    suspend fun delete(balance: Balance) {
        balanceDao.delete(balance)
    }

    fun getBalanceOfUser(): LiveData<Balance> {
        return balanceDao.getBalanceOfUser()
    }

}