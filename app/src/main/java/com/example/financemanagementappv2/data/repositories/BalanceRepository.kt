package com.example.financemanagementappv2.data.repositories

import com.example.financemanagementappv2.data.dao.BalanceDao
import com.example.financemanagementappv2.data.entities.Balance
import com.example.financemanagementappv2.data.enums.PeriodTab
import com.example.financemanagementappv2.helpers.DateHelper
import com.example.financemanagementappv2.helpers.DateHelper.days
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class BalanceRepository(private val balanceDao: BalanceDao) {

    suspend fun insert(balance: Balance) {
        balanceDao.insert(balance)
    }

    suspend fun delete(balance: Balance) {
        balanceDao.delete(balance)
    }

    fun getBalanceOfUser(): Flow<Balance> {
        return balanceDao.getLatestBalanceOfUser()
    }

    fun getBalanceSnapshotsOfLastMonthOfUser(): Flow<List<Balance>> {
        return balanceDao.getBalanceSnapshotsOfPeriodOfUser(
            DateHelper.getStartOfCurrentMonthInMillis(),
            DateHelper.getEndOfCurrentMonthInMillis()
        )
    }

    fun getBalanceSnapshotsOfLastYearOfUser(): Flow<List<Balance>> {
        return balanceDao.getBalanceSnapshotsOfPeriodOfUser(
            DateHelper.getStartOfCurrentYearInMillis(),
            DateHelper.getEndOfCurrentYearInMillis()
        )
    }

    fun getFormattedBalanceDataForPeriod(periodTab: PeriodTab, balanceData: List<Balance>): List<Pair<Long, Double>> {
        val calendar = Calendar.getInstance()
        val entries = mutableListOf<Pair<Long, Double>>()

        when (periodTab) {
            PeriodTab.Week -> {
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.DAY_OF_YEAR, -6)

                val previousBalances = balanceData.filter { it.timestamp < calendar.timeInMillis }
                var lastKnown = previousBalances.lastOrNull()?.amount ?: 0.0

                repeat(7) {
                    val dayStart = calendar.timeInMillis
                    val dayEnd = dayStart + 1.days

                    val dailyBalances = balanceData.filter { it.timestamp in dayStart..<dayEnd }
                    val dayBalance = if (dailyBalances.isNotEmpty()) {
                        dailyBalances.last().amount
                    } else {
                        lastKnown
                    }
                    lastKnown = dayBalance

                    entries += Pair(dayStart, dayBalance)
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            PeriodTab.Month -> {
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                val previousBalances = balanceData.filter { it.timestamp < calendar.timeInMillis }
                var lastKnown = previousBalances.lastOrNull()?.amount ?: 0.0

                repeat(daysInMonth) {
                    val dayStart = calendar.timeInMillis
                    val dayEnd = dayStart + 1.days

                    val dailyBalances = balanceData.filter { it.timestamp in dayStart..<dayEnd }
                    val dayBalance = if (dailyBalances.isNotEmpty()) {
                        dailyBalances.last().amount
                    } else {
                        lastKnown
                    }
                    lastKnown = dayBalance

                    entries += Pair(dayStart, dayBalance)
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            PeriodTab.SixMonths -> {
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                calendar.add(Calendar.MONTH, -5) // go back 5 months + this one = 6
                val previousBalances = balanceData.filter { it.timestamp < calendar.timeInMillis }
                var lastKnown = previousBalances.lastOrNull()?.amount ?: 0.0

                repeat(6) {
                    val monthStart = calendar.timeInMillis

                    calendar.add(Calendar.MONTH, 1)
                    val monthEnd = calendar.timeInMillis

                    val monthlyBalances = balanceData.filter { it.timestamp in monthStart..<monthEnd }
                    val monthBalance = if (monthlyBalances.isNotEmpty()) {
                        monthlyBalances.last().amount
                    } else {
                        lastKnown
                    }
                    lastKnown = monthBalance

                    entries += Pair(monthStart, monthBalance)
                }
            }
            PeriodTab.OneYear -> {
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                calendar.add(Calendar.MONTH, -11) // 12 months including current
                val previousBalances = balanceData.filter { it.timestamp < calendar.timeInMillis }
                var lastKnown = previousBalances.lastOrNull()?.amount ?: 0.0

                repeat(12) {
                    val monthStart = calendar.timeInMillis

                    calendar.add(Calendar.MONTH, 1)
                    val monthEnd = calendar.timeInMillis

                    val monthlyBalances = balanceData.filter { it.timestamp in monthStart..<monthEnd }
                    val monthBalance = if (monthlyBalances.isNotEmpty()) {
                        monthlyBalances.last().amount
                    } else {
                        lastKnown
                    }
                    lastKnown = monthBalance

                    entries += Pair(monthStart, monthBalance)
                }
            }
        }

        return entries
    }

}