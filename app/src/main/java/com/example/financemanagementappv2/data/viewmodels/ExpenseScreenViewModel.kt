package com.example.financemanagementappv2.data.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.entities.Expenses
import com.example.financemanagementappv2.data.repositories.CategoriesRepository
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
        observeUiState()
    }

    private fun observeUiState() {
        viewModelScope.launch {
            combine(
                expensesRepository.getAllExpensesOfCurrentMonthOfUser(),
                categoriesRepository.getAllExpenseCategories()
            ) { expensesRepositoryData, categoriesRepositoryData ->
                Log.d("VM -> ", "expensesRepositoryData: $expensesRepositoryData")

                ExpenseScreenUiState(
                    monthlyExpenses = expensesRepositoryData,
                    allExpenseCategories = categoriesRepositoryData,
                    categoryExpensesMapped = mapExpensesInPercentToCategories(
                        expensesRepositoryData,
                        categoriesRepositoryData
                    )
                )
            }.collect { uiState ->
                _uiState.value = uiState
            }
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
    }

    private fun mapExpensesInPercentToCategories(expenses: List<Expenses>, categories: List<Categories>): Map<String, Double> {
        if (expenses.isEmpty()) return emptyMap()

        val total = expenses.sumOf { it -> it.amount }

        val amountByCategory: Map<Long, Double> = expenses.groupBy { it.categoryId }.mapValues { entry -> entry.value.sumOf { it.amount } }

        Log.d("VM ->", "amountByCategory: $amountByCategory")

        return categories
            .filter { category -> (amountByCategory[category.id] ?: 0.0) > 0.0 }
            .associate { category ->
                val categoryTotal = amountByCategory[category.id]!!
                category.name to (categoryTotal / total) * 100
            }
    }
}

data class ExpenseScreenUiState(
    val monthlyExpenses: List<Expenses> = emptyList<Expenses>(),
    val allExpenseCategories: List<Categories> = emptyList<Categories>(),
    val categoryExpensesMapped: Map<String, Double> = emptyMap<String, Double>()
)