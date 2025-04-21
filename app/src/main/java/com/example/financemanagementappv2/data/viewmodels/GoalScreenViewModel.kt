package com.example.financemanagementappv2.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagementappv2.data.entities.FinancialGoals
import com.example.financemanagementappv2.data.repositories.FinancialGoalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalScreenViewModel @Inject constructor(
    private val financialGoalsRepository: FinancialGoalsRepository
): ViewModel() {

    fun insertGoal(goalName: String, startDate: Long, endDate: Long, targetAmount: Double) {
        viewModelScope.launch {
            financialGoalsRepository.insertAll(
                FinancialGoals(
                    goalName = goalName,
                    targetAmount = targetAmount,
                    createdAt = startDate,
                    dueDate = endDate,
                )
            )
        }
    }

}