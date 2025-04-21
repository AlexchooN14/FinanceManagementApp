package com.example.financemanagementappv2.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.entities.Incomes
import com.example.financemanagementappv2.data.repositories.CategoriesRepository
import com.example.financemanagementappv2.data.repositories.IncomesRepository
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
class IncomeScreenViewModel @Inject constructor(
    private val incomesRepository: IncomesRepository,
    private val categoriesRepository: CategoriesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(IncomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        obtainData()
    }

    val categoryIncomesMapped: StateFlow<Map<String, Double>> =
        _uiState
            .map { uiState ->
                mapIncomesInPercentToCategories(
                    uiState.monthlyIncomes,
                    uiState.allIncomeCategories
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )

    private fun obtainData() {
        viewModelScope.launch {

            val monthlyIncomes = incomesRepository.getAllIncomesOfCurrentMonthOfUser().first()
            val allIncomeCategories = categoriesRepository.getAllIncomeCategories().first()

            _uiState.value = IncomeScreenUiState(
                monthlyIncomes = monthlyIncomes,
                allIncomeCategories = allIncomeCategories,
            )
        }
    }

    fun insertIncome(incomeAmount: Double, category: Categories) {
        viewModelScope.launch {
            incomesRepository.insertAll(
                Incomes(
                    amount = incomeAmount,
                    description = "",
                    date = System.currentTimeMillis(),
                    categoryId = category.id
                )
            )
        }
        obtainData()
    }


    private fun mapIncomesInPercentToCategories(incomes: List<Incomes>, categories: List<Categories>): Map<String, Double> {

        val totalIncome = incomes.sumOf { it -> it.amount }

        if (totalIncome == 0.0) return emptyMap()

        val incomeByCategory: Map<Long, Double> = incomes.groupBy { it.categoryId }.mapValues { entry -> entry.value.sumOf { it.amount } }

        return categories.associate { category ->
            val categoryTotal = incomeByCategory[category.id] ?: 0.0
            category.name to (categoryTotal / totalIncome) * 100
        }
    }
}

data class IncomeScreenUiState(
    val monthlyIncomes: List<Incomes> = emptyList<Incomes>(),
    val allIncomeCategories: List<Categories> = emptyList<Categories>()
)
