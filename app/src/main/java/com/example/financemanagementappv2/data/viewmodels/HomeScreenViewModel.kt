package com.example.financemanagementappv2.data.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.Balance
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.data.entities.FinancialGoals
import com.example.financemanagementappv2.data.entities.Incomes
import com.example.financemanagementappv2.data.enums.CategoryFinancialType
import com.example.financemanagementappv2.data.enums.PeriodTab
import com.example.financemanagementappv2.data.repositories.BalanceRepository
import com.example.financemanagementappv2.data.repositories.CategoriesRepository
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
import com.example.financemanagementappv2.data.repositories.FinancialGoalsRepository
import com.example.financemanagementappv2.data.repositories.IncomesRepository
import com.example.financemanagementappv2.helpers.DateHelper.getTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
    private val financialGoalsRepository: FinancialGoalsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val applicationScope: CoroutineScope
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _balanceSelectedTab = MutableStateFlow(PeriodTab.Week)
    val balanceSelectedTab = _balanceSelectedTab.asStateFlow()

    init {
        observeUiState()
    }

    fun onTabSelected(tab: PeriodTab) {
        _balanceSelectedTab.value = tab
    }

    private fun observeUiState() {
        viewModelScope.launch {
            combine(
                _balanceSelectedTab,
                incomesRepository.getAllIncomesOfCurrentMonthOfUser(),
                expensesRepository.getAllExpensesOfCurrentMonthOfUser(),
                balanceRepository.getBalanceOfUser(),
                financialGoalsRepository.getAllFinancialGoalsOfUser(),
            ) { selectedTab, incomeRepositoryData, expenseRepositoryData, balanceRepositoryData, financialGoalsRepositoryData ->

                val monthlyIncomeSum = incomeRepositoryData.sumOf { it.amount }
                val monthlyExpenseSum = expenseRepositoryData.sumOf { it.amount }
                val currentBalance = balanceRepositoryData?.amount ?: 0.0
                val financialGoals = financialGoalsRepositoryData
                val yearlyBalanceData =
                    balanceRepository.getBalanceSnapshotsOfLastYearOfUser().first()

                val circularOverlayIncomeProgress = if (currentBalance <= 0.0) {
                    if (monthlyIncomeSum == 0.0) 0f else 1f
                } else {
                    (monthlyIncomeSum / currentBalance).coerceIn(0.0, 1.0).toFloat()
                }

                val circularOverlayExpenseProgress = if (currentBalance <= 0.0) {
                    if (monthlyExpenseSum == 0.0) 0f else 0.5f
                } else {
                    (monthlyExpenseSum / currentBalance).coerceIn(0.0, 1.0).toFloat()
                }

                HomeScreenUiState(
                    balanceSelectedTab = selectedTab,
                    monthlyIncomeSum = monthlyIncomeSum,
                    monthlyExpenseSum = monthlyExpenseSum,
                    circularOverlayIncomeProgress = circularOverlayIncomeProgress,
                    circularOverlayExpenseProgress = circularOverlayExpenseProgress,
                    filteredBalanceData = balanceRepository.getFormattedBalanceDataForPeriod(
                        selectedTab,
                        yearlyBalanceData
                    ),
                    currentBalance = currentBalance,
                    financialGoals = financialGoals
                )
            }.collect { uiState ->
                _uiState.value = uiState
            }
        }
    }

    fun labelFormatter(timestamp: Long): String {
        val formatter = when (balanceSelectedTab.value) {
            PeriodTab.Week -> SimpleDateFormat("EEE", Locale.ENGLISH)
            PeriodTab.Month -> SimpleDateFormat("dd", Locale.ENGLISH)
            PeriodTab.SixMonths -> SimpleDateFormat("MMM", Locale.ENGLISH)
            PeriodTab.OneYear -> SimpleDateFormat("MMM", Locale.ENGLISH)
        }
        return formatter.format(Date(timestamp))
    }

    fun insertDummyData() {
        applicationScope.launch {
            categoriesRepository.insertAll(dummyCategories)

            val allEntries = (dummyIncomes.map { it as Any } + dummyExpenses.map { it as Any })
                .sortedBy { entry ->
                    when (entry) {
                        is Incomes -> entry.date
                        is Expenses -> entry.date
                        else -> Long.MAX_VALUE
                    }
                }

            Log.d("DummyFunction", "allEntries: $allEntries")

            // Insert one-by-one as if user added them manually
            allEntries.forEach { entry ->
                when (entry) {
                    is Incomes -> incomesRepository.insertAll(entry)
                    is Expenses -> expensesRepository.insertAll(entry)
                }
            }

            financialGoalsRepository.insertAll(dummyGoals)
        }
    }
}

data class HomeScreenUiState(
    val balanceSelectedTab: PeriodTab = PeriodTab.Week,
    val monthlyIncomeSum: Double = 0.0,
    val monthlyExpenseSum: Double = 0.0,
    val circularOverlayIncomeProgress: Float = 0f,
    val circularOverlayExpenseProgress: Float = 0f,
    val filteredBalanceData: List<Pair<Long, Double>> = emptyList<Pair<Long, Double>>(),
    val currentBalance: Double = 0.0,
    val financialGoals: List<FinancialGoals> = emptyList<FinancialGoals>()
)

val dummyCategories: List<Categories> = listOf(
    Categories(name = "Salary", type = CategoryFinancialType.INCOME),
    Categories(name = "Freelancing", type = CategoryFinancialType.INCOME),
    Categories(name = "Investments", type = CategoryFinancialType.INCOME),
    Categories(name = "Bonus", type = CategoryFinancialType.INCOME),
    Categories(name = "Side Business", type = CategoryFinancialType.INCOME),
    Categories(name = "Rental Income", type = CategoryFinancialType.INCOME),

    // Expense Categories
    Categories(name = "Groceries", type = CategoryFinancialType.EXPENSE),
    Categories(name = "Rent", type = CategoryFinancialType.EXPENSE),
    Categories(name = "Utilities", type = CategoryFinancialType.EXPENSE),
    Categories(name = "Dining Out", type = CategoryFinancialType.EXPENSE),
    Categories(name = "Subscriptions", type = CategoryFinancialType.EXPENSE),
    Categories(name = "Transport", type = CategoryFinancialType.EXPENSE),
    Categories(name = "Entertainment", type = CategoryFinancialType.EXPENSE)
)

val dummyIncomes = listOf(
    Incomes(amount = 2000.0, description = "Monthly Salary", date = getTimestamp(2025, 0, 3), categoryId = 1),
    Incomes(amount = 500.0, description = "Freelance Logo Design", date = getTimestamp(2025, 0, 17), categoryId = 2),

    Incomes(amount = 2100.0, description = "Monthly Salary", date = getTimestamp(2025, 1, 5), categoryId = 1),
    Incomes(amount = 600.0, description = "Consulting", date = getTimestamp(2025, 1, 19), categoryId = 2),

    Incomes(amount = 2100.0, description = "Monthly Salary", date = getTimestamp(2025, 2, 3), categoryId = 1),
    Incomes(amount = 550.0, description = "Crypto Trading", date = getTimestamp(2025, 2, 21), categoryId = 3),

    Incomes(amount = 2100.0, description = "Monthly Salary", date = getTimestamp(2025, 3, 4), categoryId = 1),
    Incomes(amount = 480.0, description = "Course Sales", date = getTimestamp(2025, 3, 14), categoryId = 2)
)

val dummyExpenses = listOf(
    Expenses(amount = 1000.0, description = "Rent", date = getTimestamp(2025, 0, 4), categoryId = 8),
    Expenses(amount = 150.0, description = "Groceries", date = getTimestamp(2025, 0, 10), categoryId = 7),
    Expenses(amount = 200.0, description = "Birthday Present 1", date = getTimestamp(2025, 0, 11), categoryId = 13),
    Expenses(amount = 120.0, description = "Birthday Present 2", date = getTimestamp(2025, 0, 14), categoryId = 13),
    Expenses(amount = 250.0, description = "Movies & Dining", date = getTimestamp(2025, 0, 22), categoryId = 13),

    Expenses(amount = 1000.0, description = "Rent", date = getTimestamp(2025, 1, 1), categoryId = 8),
    Expenses(amount = 140.0, description = "Groceries", date = getTimestamp(2025, 1, 11), categoryId = 7),
    Expenses(amount = 70.0, description = "Streaming & Bars", date = getTimestamp(2025, 1, 23), categoryId = 13),

    Expenses(amount = 1000.0, description = "Rent", date = getTimestamp(2025, 2, 1), categoryId = 8),
    Expenses(amount = 300.0, description = "Groceries", date = getTimestamp(2025, 2, 12), categoryId = 7),
    Expenses(amount = 280.0, description = "Weekend Trip", date = getTimestamp(2025, 2, 25), categoryId = 13),

    Expenses(amount = 1000.0, description = "Rent", date = getTimestamp(2025, 3, 1), categoryId = 8),
    Expenses(amount = 100.0, description = "Groceries", date = getTimestamp(2025, 3, 10), categoryId = 7),
    Expenses(amount = 170.0, description = "Alcohol", date = getTimestamp(2025, 3, 11), categoryId = 7),
    Expenses(amount = 100.0, description = "Games & Subscriptions", date = getTimestamp(2025, 3, 20), categoryId = 11)
)


val dummyGoals = listOf<FinancialGoals>(
    FinancialGoals(
        goalName = "Vacation to Greece",
        targetAmount = 4000.0,
        dueDate = System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000 // in 60 days
    ),
    FinancialGoals(
        goalName = "New Laptop",
        targetAmount = 1200.0,
        dueDate = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000 // in 30 days
    ),
    FinancialGoals(
        goalName = "Emergency Fund",
        targetAmount = 5000.0,
        dueDate = System.currentTimeMillis() + 180L * 24 * 60 * 60 * 1000 // in 180 days
    )
)

