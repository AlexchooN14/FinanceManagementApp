package com.example.financemanagementappv2.data.repositories

import com.example.financemanagementappv2.data.dao.ExpensesDao
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.helpers.DateHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class ExpensesRepository(private val expensesDao: ExpensesDao) {

    suspend fun insertAll(vararg expenses: Expenses) {
        expensesDao.insertAll(*expenses)
    }

    suspend fun delete(expense: Expenses) {
        expensesDao.delete(expense)
    }

    fun getAllExpensesByUserId(): Flow<List<Expenses>> {
        return expensesDao.getAllExpensesOfUser()
    }


    fun getLastExpenseByUserId(): Flow<Expenses> {
        return expensesDao.getLastExpenseOfUser()
    }


    fun getAllExpensesOfCurrentMonthOfUser(): Flow<List<Expenses>> {
        return expensesDao.getAllExpensesOfPeriodOfUser(
            DateHelper.getStartOfCurrentMonthInMillis(),
            DateHelper.getEndOfCurrentMonthInMillis()
        )
    }

    suspend fun getSumOfAllExpensesOfCurrentMonthOfUser(): Double {
        val expenses = getAllExpensesOfCurrentMonthOfUser().first()
        return expenses.sumOf { it.amount }
    }

    fun getAllExpensesOfCurrentYearOfUser(): Flow<List<Expenses>> {
        return expensesDao.getAllExpensesOfPeriodOfUser(
            DateHelper.getStartOfCurrentYearInMillis(),
            DateHelper.getEndOfCurrentYearInMillis()
        )
    }
}