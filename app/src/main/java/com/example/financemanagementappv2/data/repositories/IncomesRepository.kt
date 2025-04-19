package com.example.financemanagementappv2.data.repositories

import com.example.financemanagementappv2.data.dao.BalanceDao
import com.example.financemanagementappv2.data.dao.IncomesDao
import com.example.financemanagementappv2.data.entities.Balance
import com.example.financemanagementappv2.data.entities.Incomes
import com.example.financemanagementappv2.helpers.DateHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class IncomesRepository(private val incomesDao: IncomesDao, private val balanceDao: BalanceDao) {

    suspend fun insertAll(vararg incomes: Incomes) {
        incomesDao.insertAll(*incomes)

        val incomesSum = incomes.sumOf { it.amount }
        val latestBalance = balanceDao.getLatestBalanceOfUser().first()?.amount ?: 0.0
        val newBalance = latestBalance + incomesSum
        balanceDao.insert(Balance(amount = newBalance, timestamp = System.currentTimeMillis()))
    }

    suspend fun delete(income: Incomes) {
        incomesDao.delete(income)
    }

    fun getAllIncomesOfUser(): Flow<List<Incomes>> {
        return incomesDao.getAllIncomesOfUser()
    }

    fun getLastIncomeOfUser(): Flow<Incomes> {
        return incomesDao.getLastIncomeOfUser()
    }

    fun getAllIncomesOfCurrentMonthOfUser(): Flow<List<Incomes>> {
        return incomesDao.getAllIncomesOfPeriodOfUser(
            DateHelper.getStartOfCurrentMonthInMillis(),
            DateHelper.getEndOfCurrentMonthInMillis()
        )
    }

    suspend fun getSumOfAllIncomesOfCurrentMonthOfUser(): Double {
        val incomes = getAllIncomesOfCurrentMonthOfUser().first()
        return incomes.sumOf { it.amount }
    }

    fun getAllIncomesOfCurrentYearOfUser(): Flow<List<Incomes>> {
        return incomesDao.getAllIncomesOfPeriodOfUser(
            DateHelper.getStartOfCurrentYearInMillis(),
            DateHelper.getEndOfCurrentYearInMillis()
        )
    }

}