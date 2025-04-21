package com.example.financemanagementappv2.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.Categories
import com.example.financemanagementappv2.data.entities.Incomes
import com.example.financemanagementappv2.data.repositories.CategoriesRepository
import com.example.financemanagementappv2.data.repositories.IncomesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
        observeUiState()
    }

    private fun observeUiState() {
        viewModelScope.launch {
            combine(
                incomesRepository.getAllIncomesOfCurrentMonthOfUser(),
                categoriesRepository.getAllIncomeCategories()
            ) { incomesRepositoryData, categoriesRepositoryData ->

                IncomeScreenUiState(
                    monthlyIncomes = incomesRepositoryData,
                    allIncomeCategories = categoriesRepositoryData,
                    categoryIncomesMapped = mapIncomesInPercentToCategories(incomesRepositoryData, categoriesRepositoryData)
                )
            }.collect { uiState ->
                _uiState.value = uiState
            }
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
    }


    private fun mapIncomesInPercentToCategories(incomes: List<Incomes>, categories: List<Categories>): Map<String, Double> {
        if (incomes.isEmpty()) return emptyMap()

        val totalIncome = incomes.sumOf { it -> it.amount }

        val incomeByCategory: Map<Long, Double> = incomes.groupBy { it.categoryId }.mapValues { entry -> entry.value.sumOf { it.amount } }

        return categories
            .filter { category -> (incomeByCategory[category.id] ?: 0.0) > 0.0 }
            .associate { category ->
                val categoryTotal = incomeByCategory[category.id]!!
                category.name to (categoryTotal / totalIncome) * 100
            }
    }
}

data class IncomeScreenUiState(
    val monthlyIncomes: List<Incomes> = emptyList<Incomes>(),
    val allIncomeCategories: List<Categories> = emptyList<Categories>(),
    val categoryIncomesMapped: Map<String, Double> = emptyMap<String, Double>()
)
