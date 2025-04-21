package com.example.financemanagementappv2.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.data.repositories.CategoriesRepository
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseScreenViewModel @Inject constructor(
    private val expensesRepository: ExpensesRepository,
    private val categoriesRepository: CategoriesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ExpenseScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        obtainData()
    }

    val categoryExpensesMapped: StateFlow<Map<String, Double>> =
        _uiState
            .map { uiState ->
                mapExpensesInPercentToCategories(
                    uiState.monthlyExpenses,
                    uiState.allExpenseCategories
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )

    private fun obtainData() {
        viewModelScope.launch {

            val monthlyExpenses = expensesRepository.getAllExpensesOfCurrentMonthOfUser().first()
            val allIncomeCategories = categoriesRepository.getAllExpenseCategories().first()

            _uiState.value = ExpenseScreenUiState(
                monthlyExpenses = monthlyExpenses,
                allExpenseCategories = allIncomeCategories,
            )
        }
    }

    fun insertExpense(amount: Double, category: Categories) {
        viewModelScope.launch {
            expensesRepository.insertAll(
                Expenses(
                    amount = amount,
                    description = "",
                    date = System.currentTimeMillis(),
                    categoryId = category.id
                )
            )
        }
        obtainData()
    }


    private fun mapExpensesInPercentToCategories(expenses: List<Expenses>, categories: List<Categories>): Map<String, Double> {

        val totalIncome = expenses.sumOf { it -> it.amount }

        if (totalIncome == 0.0) return emptyMap()

        val incomeByCategory: Map<Long, Double> = expenses.groupBy { it.categoryId }.mapValues { entry -> entry.value.sumOf { it.amount } }

        return categories.associate { category ->
            val categoryTotal = incomeByCategory[category.id] ?: 0.0
            category.name to (categoryTotal / totalIncome) * 100
        }
    }
}

data class ExpenseScreenUiState(
    val monthlyExpenses: List<Expenses> = emptyList<Expenses>(),
    val allExpenseCategories: List<Categories> = emptyList<Categories>()
)