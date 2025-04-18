package com.example.financemanagementappv2.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.repositories.ExpensesRepository
import com.example.financemanagementappv2.data.repositories.IncomesRepository
import com.example.financemanagementappv2.helpers.DateHelper
import com.example.financemanagementappv2.helpers.Graphable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val incomesRepository: IncomesRepository,
    private val expensesRepository: ExpensesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        obtainData()
    }

    private fun obtainData() {
        viewModelScope.launch {
            val monthlyIncomeSum = incomesRepository.getSumOfAllIncomesOfCurrentMonthOfUser()
            val monthlyExpenseSum = expensesRepository.getSumOfAllExpensesOfCurrentMonthOfUser()

            _uiState.value = HomeScreenUiState(
                monthlyIncomeSum = monthlyIncomeSum,
                monthlyExpenseSum = monthlyExpenseSum,
                monthlyBalanceData = listOf(
                    object : Graphable {
                        override fun getData(): Double {
                            return 100.0
                        }

                        override fun getDataTimestamp(): Long {
                            return DateHelper.getStartOfCurrentYearInMillis()
                        }
                    },
                    object : Graphable {
                        override fun getData(): Double {
                            return 200.0
                        }

                        override fun getDataTimestamp(): Long {
                            return DateHelper.getStartOfCurrentMonthInMillis()
                        }
                    },
                    object : Graphable {
                        override fun getData(): Double {
                            return 300.0
                        }

                        override fun getDataTimestamp(): Long {
                            return DateHelper.getDateForDay(10)
                        }
                    }
                )
            )
        }
    }
}

data class HomeScreenUiState(
    val monthlyIncomeSum: Double = 0.0,
    val monthlyExpenseSum: Double = 0.0,
    val monthlyBalanceData: List<Graphable> = emptyList()
)

