package com.example.financemanagementappv2.data.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.Balance
import com.example.financemanagementappv2.data.entities.FinancialGoals
import com.example.financemanagementappv2.data.enums.PeriodTab
import com.example.financemanagementappv2.data.repositories.BalanceRepository
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
import com.example.financemanagementappv2.data.repositories.FinancialGoalsRepository
import com.example.financemanagementappv2.data.repositories.IncomesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val incomesRepository: IncomesRepository,
    private val expensesRepository: ExpensesRepository,
    private val balanceRepository: BalanceRepository,
    private val financialGoalsRepository: FinancialGoalsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _balanceSelectedTab = MutableStateFlow(PeriodTab.Week)
    val balanceSelectedTab = _balanceSelectedTab.asStateFlow()

    init {
        obtainData()
    }

    fun onTabSelected(tab: PeriodTab) {
        _balanceSelectedTab.value = tab
    }

    internal fun obtainData() {
        viewModelScope.launch {
            val monthlyIncomeSum = incomesRepository.getSumOfAllIncomesOfCurrentMonthOfUser()
            val monthlyExpenseSum = expensesRepository.getSumOfAllExpensesOfCurrentMonthOfUser()
            val yearlyBalanceData = balanceRepository.getBalanceSnapshotsOfLastYearOfUser()
            val currentBalance = balanceRepository.getBalanceOfUser().firstOrNull()
            var balanceAmount = currentBalance?.amount ?: 0.0
            val financialGoals = financialGoalsRepository.getAllFinancialGoalsOfUser().first()

            val circularOverlayIncomeProgress = if (balanceAmount <= 0.0) {
                if (monthlyIncomeSum == 0.0) 0f else 1f
            } else {
                (monthlyIncomeSum / balanceAmount).coerceIn(0.0, 1.0).toFloat()
            }

            val circularOverlayExpenseProgress = if (balanceAmount <= 0.0) {
                if (monthlyExpenseSum == 0.0) 0f else 1f
            } else {
                (monthlyExpenseSum / balanceAmount).coerceIn(0.0, 1.0).toFloat()
            }

            _uiState.value = HomeScreenUiState(
                monthlyIncomeSum = monthlyIncomeSum,
                monthlyExpenseSum = monthlyExpenseSum,
                circularOverlayIncomeProgress = circularOverlayIncomeProgress,
                circularOverlayExpenseProgress = circularOverlayExpenseProgress,
                yearlyBalanceData = yearlyBalanceData,
                currentBalance = balanceAmount,
                financialGoals = financialGoals
            )
        }
    }

    val filteredBalanceData: StateFlow<List<Pair<Long, Double>>> =
        combine(
            _balanceSelectedTab,
            _uiState.map { it.yearlyBalanceData }  // Make sure you're observing the data!
        ) { selectedTab, yearlyData ->
            balanceRepository.getFormattedBalanceDataForPeriod(selectedTab, yearlyData)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun labelFormatter(timestamp: Long): String {
        val formatter = when (balanceSelectedTab.value) {
            PeriodTab.Week -> SimpleDateFormat("EEE", Locale.ENGLISH)
            PeriodTab.Month -> SimpleDateFormat("dd", Locale.ENGLISH)
            PeriodTab.SixMonths -> SimpleDateFormat("MMM", Locale.ENGLISH)
            PeriodTab.OneYear -> SimpleDateFormat("MMM", Locale.ENGLISH)
        }
        return formatter.format(Date(timestamp))
    }
}

data class HomeScreenUiState(
    val monthlyIncomeSum: Double = 0.0,
    val monthlyExpenseSum: Double = 0.0,
    val circularOverlayIncomeProgress:Float = 0f,
    val circularOverlayExpenseProgress:Float = 0f,
    val yearlyBalanceData: List<Balance> = emptyList<Balance>(),
    val currentBalance: Double = 0.0,
    val financialGoals: List<FinancialGoals> = emptyList<FinancialGoals>()
)

