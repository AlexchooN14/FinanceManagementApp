package com.example.financemanagementappv2.data.repositories

import androidx.lifecycle.LiveData
import com.example.financemanagementappv2.data.dao.FinancialGoalsDao
import com.example.financemanagementappv2.data.entities.FinancialGoals


class FinancialGoalsRepository(private val financialGoalsDao: FinancialGoalsDao) {

    suspend fun insertAll(vararg financialGoals: FinancialGoals) {
        financialGoalsDao.insertAll(*financialGoals)
    }

    suspend fun delete(financialGoal: FinancialGoals) {
        financialGoalsDao.delete(financialGoal)
    }

    fun getAllFinancialGoalsByUserId(userId: Long): LiveData<List<FinancialGoals>> {
        return financialGoalsDao.getAllFinancialGoalsByUserId(userId)
    }

}