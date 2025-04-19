package com.example.financemanagementappv2.data.repositories

import com.example.financemanagementappv2.data.dao.FinancialGoalsDao
import com.example.financemanagementappv2.data.entities.FinancialGoals
import kotlinx.coroutines.flow.Flow


class FinancialGoalsRepository(private val financialGoalsDao: FinancialGoalsDao) {

    suspend fun insertAll(vararg financialGoals: FinancialGoals) {
        financialGoalsDao.insertAll(*financialGoals)
    }

    suspend fun delete(financialGoal: FinancialGoals) {
        financialGoalsDao.delete(financialGoal)
    }

    fun getAllFinancialGoalsOfUser(): Flow<List<FinancialGoals>> {
        return financialGoalsDao.getAllFinancialGoalsOfUser()
    }

}